enum class Player(val string:String){
    WHITE("X"),
    BLACK("O");
    fun other() = if (this == WHITE) BLACK else WHITE
}