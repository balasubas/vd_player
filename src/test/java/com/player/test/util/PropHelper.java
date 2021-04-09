package com.player.test.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class PropHelper {

    //////////////////////////////  DECLARATIONS  /////////////////////////////
    private Map<String,String> propMap;
    private String resourcesSubDir = System.getProperty("user.dir") + "/src/test/resources/test.properties";

    //////////////////////////////////////////////////////////////////////////
    public PropHelper(){
        propMap = getPropMap();
    }

    //////////////////////////////////////////////////////////////////////////
    public String getTempFileDirectory(){
        return System.getProperty("user.dir") + propMap.get("test.dir");
    }

    //////////////////////////////////////////////////////////////////////////
    public String getVideoFileDirectory(){
        return System.getProperty("user.dir") + propMap.get("vid.dir");
    }

    //////////////////////////////////////////////////////////////////////////
    private Map<String,String> getPropMap(){
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(new File(resourcesSubDir)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return props.entrySet().stream()
                .collect(Collectors.toMap((entry)-> (String)entry.getKey(),
                        (entry)-> (String)entry.getValue()
                ));
    }

}
