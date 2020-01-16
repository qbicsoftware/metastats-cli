package life.qbic.metastats.request

class ProjectNotFoundException extends IOException{

    ProjectNotFoundException(String message){
        super(message)
    }

    ProjectNotFoundException(String message, Throwable t){
        super(message,t)
    }
}
