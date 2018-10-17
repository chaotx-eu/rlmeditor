package fh.sem.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import fh.sem.App;
import fh.sem.logic.Project;

public class ProjectManager extends Observable {
    private Project activeProject;
    private List<Project> projects;

    private static ProjectManager singleton;
    private ProjectManager() {}

    public static ProjectManager instance() {
        if(singleton == null)
            singleton = new ProjectManager();

        return singleton;
    }
    
    public Project getActiveProject() {
        return activeProject;
    }

    // public void setActiveProject(int index) {
    //     if(activeProject != null)
    //         projects.add(activeProject);

    //     activeProject = projects.get(index);
    //     projects.remove(activeProject);

    //     setChanged();
    //     notifyObservers();
    // }

    public void setActiveProject(Project project) {
        if(activeProject != null && (project == null
        || !project.getTitle().equals(activeProject.getTitle())))
            projects.add(activeProject);

        activeProject = project;
        projects.remove(project);

        setChanged();
        notifyObservers();
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void delete(Project project) {
        File file = new File(App.PROJ_DIR + File.separator + project.getTitle());
        file.delete();
        load();

        setChanged();
        notifyObservers();
    }

    public void load() {
        projects = new LinkedList<>();
        File projDir = new File(App.PROJ_DIR);

        for(File file : projDir.listFiles()) {
            try(ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(file))) {
                projects.add((Project)ois.readObject());
            } catch(IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    public void save(Project project) {
        File file = new File(App.PROJ_DIR + File.separator + project.getTitle());

        try(ObjectOutputStream oos = new ObjectOutputStream(
            new FileOutputStream(file))) {
            oos.writeObject(project);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}