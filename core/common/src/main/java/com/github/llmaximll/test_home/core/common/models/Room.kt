package com.github.llmaximll.test_home.core.common.models

import androidx.annotation.StringRes
import com.github.llmaximll.test_home.core.common.R

enum class Room(
    @StringRes val titleRes: Int
) {
    FIRST(
        titleRes = R.string.core_common_room_first
    ),
    SECOND(
        titleRes = R.string.core_common_room_second
    ),
    UNDEFINED(
        titleRes = R.string.core_common_room_undefined
    );

    companion object {
        fun String.asRoom() =
            entries.find { it.name == this } ?: UNDEFINED
    }
}