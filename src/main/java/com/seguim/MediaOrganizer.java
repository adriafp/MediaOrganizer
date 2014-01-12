package com.seguim;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * User: adria
 * Date: 11/01/14
 * Time: 19:25
 */
public class MediaOrganizer {

    private File tvShows;
    private File movies;
    private File init;
    private Properties properties;
    private StringBuilder sb;

    public MediaOrganizer() throws Exception {
        sb = new StringBuilder();
        properties = new Properties();

        File file = new File(MediaOrganizer.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        File propertiesFile = new File(file.getParentFile(), "mediaorganizer.properties");
        if(!propertiesFile.exists()){
            throw new Exception(propertiesFile.getAbsolutePath() + " doesn't exists.");
        }
        FileInputStream fileInputStream = new FileInputStream(propertiesFile);
        properties.load(fileInputStream);
        fileInputStream.close();

        tvShows = new File(properties.getProperty("tvshows.dir"));
        movies = new File(properties.getProperty("movies.dir"));
        init = new File(properties.getProperty("init.dir"));
        sb.append("tvShows.getAbsoluteFile() = ").append(tvShows.getAbsoluteFile()).append("\n");
        sb.append("movies.getAbsoluteFile() = ").append(movies.getAbsoluteFile()).append("\n");
        sb.append("init.getAbsoluteFile() = ").append(init.getAbsoluteFile()).append("\n");
        if(!tvShows.exists()){
            throw new Exception(tvShows.getAbsolutePath() + " doesn't exists.");
        }
        if(!movies.exists()){
            throw new Exception(movies.getAbsolutePath() + " doesn't exists.");
        }
        if(!init.exists()){
            throw new Exception(init.getAbsolutePath() + " doesn't exists.");
        }
    }

    public void organize() throws Exception {

        String[] list = init.list();

        for (String name : list) {
            File current = new File(init,name);
            if(current.getAbsoluteFile().equals(tvShows.getAbsoluteFile()) || current.getAbsoluteFile().equals(movies.getAbsoluteFile())) {
                continue;
            }
            sb.append("name = ").append(name).append("\n");
            sb.append("isTvShow = ").append(isTvShow(name)).append("\n");
            if (isTvShow(name)) {
                String tvShowName = getTvShowName(name);
                File newTvShow = new File(tvShows, tvShowName);
                if (!existsTvShow(tvShowName)) {
                    if (newTvShow.mkdir()) {
                        sb.append("newTvShow = ").append(newTvShow).append(". Created correctly.").append("\n");
                    } else {
                        throw new Exception("Error creating folder: " + newTvShow);
                    }
                }
                File orig = new File(init, name);
                File dest = new File(newTvShow, sanitize(name));
                sb.append("Trying to move to: ").append(dest.getAbsoluteFile()).append("\n");
                if (orig.renameTo(dest)) {
                    sb.append("File moved correctly. ").append("\n");
                }
            } else {
                //is a Movie
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM.yyyy");
                String currentMonth=simpleDateFormat.format(new Date());
                sb.append("currentMonth = ").append(currentMonth).append("\n");
                File f = new File(movies, currentMonth);
                if(!f.exists()) {
                    if(f.mkdir()) {
                        sb.append("Folder created correctly").append("\n");
                    }
                }
                File dest = new File(f,name);
                sb.append("Trying to move to: ").append(dest.getAbsoluteFile()).append("\n");
                if(current.renameTo(dest)) {
                    sb.append("File moved correctly. ").append("\n");
                }
            }
        }
    }

    private boolean isTvShow(String name) {
        name = name.toUpperCase();
        int pos_S = name.indexOf("S");
        return pos_S > -1 && name.length() > pos_S + 2 && name.charAt(pos_S + 3) == 'E';
    }

    private String getTvShowName(String name) {
        name = sanitize(name);
        int pos_S = name.indexOf("S");
        return name.substring(0, pos_S).replace("."," ").trim();
    }

    private boolean existsTvShow(String name) {
        String[] list = tvShows.list();
        for (String aList : list) {
            if (aList.equals(name)) {
                return true;
            }
        }
        return false;
    }

    private String sanitize(String name) {
        String[] exceptions = properties.getProperty("exceptions").split("#SEPARATOR#");
        for(String exception : exceptions) {
            name = name.replace(exception, "");
        }
        return name.trim();
    }

    public String getResult() {
        return sb.toString();
    }
}
