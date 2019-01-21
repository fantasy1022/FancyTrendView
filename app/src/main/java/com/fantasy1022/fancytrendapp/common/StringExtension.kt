package com.fantasy1022.fancytrendapp.common


//Ex:czech republic" -> "Czech Republic"
fun String.modifyFirstCharToUpperCase(): String {
    //1.The First Character to UpperCase
    //2.The Character after empty space

    val charArray = this.toCharArray()
    for (i in charArray.indices) {
        if (i == 0) {
            charArray[0] = charArray[0].toUpperCase()
        } else if (charArray[i] == ' ') {
            if (i + 1 < charArray.size) {
                charArray[i + 1] = charArray[i + 1].toUpperCase()
            }
        }
    }

    return String(charArray)
}
