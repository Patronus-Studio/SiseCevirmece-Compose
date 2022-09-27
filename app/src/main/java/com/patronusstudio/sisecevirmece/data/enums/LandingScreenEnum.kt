package com.patronusstudio.sisecevirmece.data.enums

enum class LandingScreenEnum {
    INIT_PAGE{
        override fun pageCount(): Int  = 0

    },
    FIRST_PAGE {
        override fun pageCount(): Int = 1
    },
    SECOND_PAGE {
        override fun pageCount(): Int = 2
    },
    THIRD_PAGE {
        override fun pageCount(): Int = 3
    };

    abstract fun pageCount(): Int
}