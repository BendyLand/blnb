package bendyland.blnb.state

object State:
    private var saveOnExit: Boolean = false
    
    def setSaveOnExit(arg: Boolean) = 
        saveOnExit = arg

    def getSaveOnExit(): Boolean = 
        return saveOnExit
