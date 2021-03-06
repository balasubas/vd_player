package com.player.ui;

import com.player.entity.AudioControls;
import com.player.entity.PlayList;
import com.player.entity.PlayListItem;
import com.player.entity.VideoFileWrapper;
import com.player.service.*;
import com.player.utils.ApplicationProperties;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.stream.Collectors;


public class MainScreen implements ParentScreen {

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    @Autowired
    @Qualifier("appProperties")
    private ApplicationProperties appProperties;

    @Autowired
    @Qualifier("playerService")
    private ConsumerService consumerService;

    @Autowired
    @Qualifier("producerService")
    private ProducerService producerService;

    @Autowired
    @Qualifier("preloadService")
    private Preloader preloaderService;

    @Autowired
    @Qualifier("progressWindow")
    private ProgressWindow progressWindow;

    @Autowired
    @Qualifier("queueService")
    private QueueService queueService;

    @Autowired
    @Qualifier("fileService")
    private FileService fileService;

    private final double leftPaneWidth = 220;
    private final double gridPaneHeight = 430;
    private final double tableViewHeight = 250;
    private final double spacing = 10;
    private  FileChooser chooser;
    private TableView<VideoFileWrapper> tableView;
    private GridPane gridPane;
    private Map<File,Future<MediaPlayer>> mediaQueue;
    private Stage primaryStage;
    private Slider slider;
    private Slider volumeSlider;
    private MenuBar menuBar;
    private static Logger logger;

    //////////////////////////////////////////////////////////////////////////
    public Stage buildMainStage(){
        init();
        VBox leftSide = configureLeftPanel(new VBox(new Label("Video Files")));
        Button open = new Button("+");
        Button clear = new Button("clear");

        open.setOnAction((actionEvent)->{
            List<File> temp = chooser.showOpenMultipleDialog(primaryStage);
            if(Objects.nonNull(temp)) {
                temp.stream().filter(Objects::nonNull)
                            .forEach((file) -> queueService.addToMediaQueue(file));
            }
        });

        clear.setOnAction((actionEvent)->{
            tableView.getItems().clear();
            tableView.refresh();
            if(!gridPane.getChildren().isEmpty()){
                if(consumerService.isPlaying()){
                    consumerService.stop();
                }

                gridPane.getChildren().forEach((node)->{
                    if(node.getClass().equals(MediaView.class)){
                        ((MediaView) node).getMediaPlayer().dispose();
                    }
                });

                gridPane.getChildren().stream()
                        .filter((node)-> node.getClass().equals(MediaView.class))
                        .map((node)-> ((MediaView) node).getMediaPlayer())
                        .forEach(MediaPlayer::dispose);

                gridPane.getChildren()
                        .removeIf((node)-> node.getClass().equals(MediaView.class));
            }
        });

        open.setId("open-btn");
        open.setTooltip(buildToolTip(appProperties.getOpenBtnToolTip(),null));

        clear.setId("clear-btn");
        clear.setTooltip(buildToolTip(appProperties.getClearBtnToolTip(), null));

        HBox leftSideHbox = new HBox();
        leftSideHbox.setSpacing(10);
        // The Label here is just for spacing
        leftSideHbox.getChildren().addAll(new Label(" "),open,clear);

        leftSide.getChildren().add(leftSideHbox);
        leftSide.setMaxWidth(leftPaneWidth);
        leftSide.setMinWidth(leftPaneWidth);

        VBox rightSide = new VBox();
        rightSide.setSpacing(10.0);
        HBox hBox = configureVidControls();

        slider = builStandardSlider( "slide", 0, 100, 0.5, 30, 1000);
        consumerService.setSlider(slider);

        gridPane = new GridPane();
        gridPane.setMinHeight(gridPaneHeight);

        HBox volHbox = new HBox();
        volHbox.setSpacing(10.0);
        volumeSlider = builStandardSlider( "vol-slide", 0, 10, 0.0, 30, 500);

        RadioButton muteBtn = new RadioButton("Mute");
        muteBtn.setId("mute-btn");
        AudioControls audioControls = new AudioControls(volumeSlider, muteBtn);
        volHbox.getChildren().addAll(new Label("    Volume:"),volumeSlider,muteBtn);

        consumerService.setAudioControls(audioControls);

        VBox controlsVbox = new VBox();
        controlsVbox.setSpacing(10.0);
        controlsVbox.getChildren().addAll(slider,volHbox);

        rightSide.getChildren().addAll(menuBar,gridPane,controlsVbox,hBox);

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(leftSide,rightSide);

        Scene scene = new Scene(splitPane,appProperties.getStdWidth(), appProperties.getStdHeight());
        scene.getStylesheets().add(appProperties.getStyleSheet());
        primaryStage.setScene(scene);
        primaryStage.setTitle(appProperties.getMainTitle());
        primaryStage.setResizable(false);

        return primaryStage;
    }

    //////////////////////////////////////////////////////////////////////////
    private VBox configureLeftPanel(VBox vBox){
        tableView = new TableView<>();
        tableView.setMinHeight(tableViewHeight);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableView.TableViewSelectionModel<VideoFileWrapper> selectionModel = tableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setSelectionModel(selectionModel);
        configTableColumns();
        vBox.getChildren().add(tableView);
        vBox.setSpacing(spacing);

        return vBox;
    }

    //////////////////////////////////////////////////////////////////////////
    private HBox configureVidControls(){
        Button pause = setImage(new Button(), "pause","ui-control");
        pause.setOnAction((action)->{
            consumerService.pause();
        });

        Button stop = setImage(new Button(), "stop","ui-control");
        stop.setOnAction((action)->{
            consumerService.stop();
        });

        Button rewind = setImage(new Button(), "back", "ui-control");
        rewind.setOnAction((actionEvent)->{
            consumerService.rewind();
        });


        Button play = setImage(new Button(), "play","ui-control");
        play.setOnAction((actionEvent)->{

            if(!tableView.getItems().isEmpty()) {
                ObservableList<VideoFileWrapper> selectedItems =
                        tableView.getSelectionModel().getSelectedItems();

               if(consumerService.isPaused()){
                   consumerService.resume();
               } else if (Objects.nonNull(selectedItems) && selectedItems.size() == 1) {
                    VideoFileWrapper videoFileWrapper = selectedItems.get(0);
                    if (videoFileWrapper != null) {
                        consumerService.play(videoFileWrapper, gridPane, producerService);
                    }
                } else {
                    queue();
                    consumerService.playFromQueue(gridPane);
                }
            }
        });

        Button fastForward = setImage(new Button(), "forward","ui-control");
        fastForward.setOnAction((event)->{
            if(consumerService.isPlaying()){
                consumerService.forward(appProperties.getStdRateIncrease());
            }
        });

        HBox hBox = buildHbox("hbox-main", appProperties.getHboxHeight(),
                                               appProperties.getHboxWidth(),
                                               Pos.BASELINE_CENTER);

        hBox.getChildren().addAll(pause,rewind,play,fastForward,stop);

        return hBox;
    }

    //////////////////////////////////////////////////////////////////////////
    private void queue(){
        if(!tableView.getSelectionModel().getSelectedCells().isEmpty()) {
            List<Integer> selectedCells = tableView.getSelectionModel().getSelectedCells()
                    .stream()
                    .map(TablePositionBase::getRow).collect(Collectors.toList());

            selectedCells.forEach((index) -> {
                producerService.add(tableView.getItems().get(index));
            });
        }else{
            producerService.load(tableView.getItems());
        }
    }

    //////////////////////////////////////////////////////////////////////////
    private Button setImage(Button button, String imageName, String id){
        Image img = new Image(appProperties.getLogo(imageName));
        ImageView view = new ImageView(img);
        view.setFitHeight(appProperties.getBtnFitHeight());
        view.setPreserveRatio(true);
        button.setGraphic(view);
        button.setId(id);
        return button;
    }

    //////////////////////////////////////////////////////////////////////////
    private void configTableColumns(){

        TableColumn<VideoFileWrapper,String> iconColumn = new TableColumn<>("Video Files");
        Callback<TableColumn<VideoFileWrapper, String>, TableCell<VideoFileWrapper, String>> cellFactory;
        cellFactory = new Callback<TableColumn<VideoFileWrapper, String>, TableCell<VideoFileWrapper, String>>(){
            @Override
            public TableCell<VideoFileWrapper, String> call(TableColumn<VideoFileWrapper, String> col) {
                ImageView imageview = new ImageView();
                final TableCell tabCell = new TableCell(){
                    @Override
                    public void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty);

                        if(getTableRow().getItem() != null) {
                            if (!empty) {
                                VideoFileWrapper videoFileWrapper =
                                        ((VideoFileWrapper) getTableRow().getItem());
                                File imgFile = videoFileWrapper.getIcon();
                                Image image = new Image(imgFile.toURI().toString());
                                imageview.setFitHeight(50.0);
                                imageview.setFitWidth(75.0);
                                imageview.setImage(image);
                                setGraphic(imageview);
                                setText(videoFileWrapper.getVideoFile().getName());
                            } else {
                                imageview.setImage(null);
                            }
                        }
                    }
                };

                return tabCell;
            }
        };

        iconColumn.setCellFactory(cellFactory);
        tableView.getColumns().add(iconColumn);
    }

    //////////////////////////////////////////////////////////////////////////
    @Scheduled(fixedRate = 1000)
    private void addVideoToTableWhenReady(){
        Map<File, Future<MediaPlayer>> pending = queueService.getPending();
        if(!pending.isEmpty()){
             Platform.runLater(()->{
                pending.entrySet().stream().forEach((entry)->{
                    File file = entry.getKey();
                    progressWindow.addNewIndicator(entry.getValue(), file.getName());
                });

                if(!progressWindow.isShowing()){
                    progressWindow.show(primaryStage.getX());
                }
             });
        }

        List<VideoFileWrapper> videoFileWrappers = queueService.cleanQueue();
        if(!videoFileWrappers.isEmpty()){
           tableView.getItems().addAll(videoFileWrappers);
           List<VideoFileWrapper> currentVids =
                   new ArrayList<>(tableView.getItems());

           fileService.saveDefaultPlaylist(currentVids, appProperties.getPlaylistDir());

           //This is important. Otherwise you see duplicated row displays.
           tableView.refresh();
        }

        if(queueService.getMediaQueue().isEmpty()){
           if(progressWindow.isShowing()){
               Platform.runLater(()->{
                  progressWindow.hide();
               });
           }
        }

        queueService.transfer();
    }

    //////////////////////////////////////////////////////////////////////////
    private void init(){
        mediaQueue = new ConcurrentHashMap<>();
        primaryStage = new Stage();
        chooser = new FileChooser();
        chooser.getExtensionFilters()
               .add(new FileChooser.ExtensionFilter("Video Files","*.mp4"));

        progressWindow.init();
        File playlistDir = new File(appProperties.getPlaylistDir());
        if(!playlistDir.exists()){
            playlistDir.mkdir();
        }

        menuBar = new MenuBar();
        menuBar.setId("menu-main");

        Menu menu = new Menu("File");
        menu.getItems().addAll(defineMenuItems());

        menuBar.getMenus().add(menu);
        logger = LogManager.getLogger(MainScreen.class);
    }

    //////////////////////////////////////////////////////////////////////////
    private List<MenuItem> defineMenuItems(){
        List<MenuItem> menuItems = new ArrayList<>();
        MenuItem savePlaylist = new MenuItem("Save Playlist");
        savePlaylist.setId("menu-item");
        savePlaylist.setOnAction((event)->{
            TextInputDialog inputDialog = new TextInputDialog("Enter a play list name");
            inputDialog.setTitle("Save To Play List");
            inputDialog.setContentText("Enter a play list name:");
            Optional<String> okWasPressed = inputDialog.showAndWait();

            if(okWasPressed.isPresent()){
                boolean doSave = true;
                String playListName = okWasPressed.get() + ".json";
                if(StringUtils.stripToEmpty(playListName).length() > 0){
                   Path savePath = Paths.get(appProperties.getPlaylistDir(), playListName);
                   if(savePath.toFile().exists()){
                      Alert fileExists = generateAlert("Playlist name already exists. Overwrite?",
                              Alert.AlertType.CONFIRMATION);

                      Optional<ButtonType> confirm = fileExists.showAndWait();
                      if(!confirm.get().getButtonData().equals(ButtonBar.ButtonData.OK_DONE)){
                          doSave = false;
                      }
                   }

                   if(doSave && !tableView.getItems().isEmpty()){
                        fileService.savePlaylist(tableView.getItems(),
                                playListName, appProperties.getPlaylistDir());
                   }
                }
            }
        });

        menuItems.add(savePlaylist);

        MenuItem loadPlayList = new MenuItem("Load Playlist");
        loadPlayList.setId("menu-item");
        loadPlayList.setOnAction((event)->{
            FileChooser loadChoice = new FileChooser();
            loadChoice.setInitialDirectory(Paths.get(appProperties.getPlaylistDir()).toFile());
            File choice = loadChoice.showOpenDialog(primaryStage);
            if(Objects.nonNull(choice)) {
                logger.info("Loading play list: " + choice.getName());
                List<File> loaded =
                        fileService.loadPlaylist(choice.getName(), appProperties.getPlaylistDir());
                PlayList autoPlayList = fileService.parseToPlayList(loaded, choice.getName());

                if (!autoPlayList.getPlayListItems().isEmpty()) {
                    tableView.getItems().clear();
                    autoPlayList.getPlayListItems()
                            .stream()
                            .map(PlayListItem::getLocation)
                            .forEach((fileItem)->{
                                queueService.addToMediaQueue(fileItem);
                            });
                }
            }
        });

        menuItems.add(loadPlayList);

        MenuItem loadAutoSaved = new MenuItem("Load Auto Saved");
        loadAutoSaved.setId("menu-item");
        loadAutoSaved.setOnAction((event)->{
            File file = fileService.loadDefaultPlaylist(appProperties.getPlaylistDir());
            if(Objects.nonNull(file)) {
                PlayList autoPlayList = fileService.parseToPlayList(Collections.singletonList(file),
                        fileService.getDefaultPlayListName());
                if (!autoPlayList.getPlayListItems().isEmpty()) {
                    autoPlayList.getPlayListItems()
                            .stream()
                            .map(PlayListItem::getLocation)
                            .forEach((fileItem)->{
                                queueService.addToMediaQueue(fileItem);
                            });
                }
            }
        });

        menuItems.add(loadAutoSaved);

        MenuItem deletePlaylist = new MenuItem("Delete A Playlist");
        deletePlaylist.setId("menu-item");
        deletePlaylist.setOnAction((event)->{
            List<File> files = null;
            List<String> fileNames = new ArrayList<>();
            fileNames.add("None");
            try {
                files = Files.list(Paths.get(appProperties.getPlaylistDir()))
                                        .map(Path::toFile)
                                        .filter((file)-> file.getName().endsWith(".json"))
                                        .filter((file)-> !file.getName().startsWith("auto_save"))
                                        .collect(Collectors.toList());

                fileNames.addAll(files.stream()
                                 .map((file)-> file.getName().replace(".json",""))
                                 .collect(Collectors.toList()));

            } catch (IOException e) {
                logger.error(e.getMessage());
            }

            ChoiceDialog<String> choiceDialog = new ChoiceDialog<String>(fileNames.get(0),fileNames);
            choiceDialog.setTitle("Delete A Playlist");
            choiceDialog.setContentText("Choose A Playlist To Delete");
            choiceDialog.showAndWait();
            String chosen = choiceDialog.getSelectedItem();
            assert files != null;
            Optional<File> toDelete = files.stream()
                                           .filter((file)-> file.getName().startsWith(chosen))
                                           .findFirst();

            if(toDelete.isPresent()){
                try {
                    File fileToDelete = toDelete.get();
                    logger.info("Deleting playlist: " + fileToDelete.getName());
                    FileUtils.forceDelete(fileToDelete);
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        });

        menuItems.add(deletePlaylist);

        return menuItems;
    }
}
