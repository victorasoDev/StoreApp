package uwu.victoraso.storeapp.model.service

interface LogService {
    fun logNonFatalCrash(throwable: Throwable?)
}