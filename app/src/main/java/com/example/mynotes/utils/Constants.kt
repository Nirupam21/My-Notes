package com.example.mynotes.utils

object Constants {

    const val NOTE_LINK: String = "NoteLink"
    const val NOTE_EXPERIMENT: String = "NoteExperiment"
    const val NOTE_LUMINOSITY: String = "NoteLuminosity"

    const val DISH_IMAGE_SOURCE_LOCAL: String = "Local"
    const val DISH_IMAGE_SOURCE_ONLINE: String = "Online"

    fun noteLink(): ArrayList<String> {
        val list = ArrayList<String>()
        list.add("breakfast")
        list.add("lunch")
        list.add("snacks")
        list.add("dinner")
        list.add("salad")
        list.add("side dish")
        list.add("dessert")
        list.add("other")

        return list
    }

    fun noteExperiment(): ArrayList<String> {
        val list = ArrayList<String>()
        list.add("Atlas")
        list.add("CMS")
        list.add("LHCb")
        list.add("others")
        return list
    }

    fun noteLuminosity(): ArrayList<String> {
        val list = ArrayList<String>()
        list.add("10")
        list.add("15")
        list.add("20")
        list.add("30")
        list.add("45")
        list.add("50")
        list.add("60")
        list.add("90")
        list.add("120")
        list.add("150")
        list.add("180")

        return list
    }


}