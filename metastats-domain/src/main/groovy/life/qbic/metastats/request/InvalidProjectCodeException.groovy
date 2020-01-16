package life.qbic.metastats.request

class InvalidProjectCodeException extends IllegalArgumentException{

    InvalidProjectCodeException(String message) {
        super(message)
    }

    InvalidProjectCodeException(String message, Throwable t) {
        super(message, t)
    }
}
