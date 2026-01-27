package com.hamit.faq

import android.support.annotation.StringRes
import com.hamit.ui.R

enum class FaqType {
    INSTALL_ERROR,        // Мод не устанавливается / ошибка импорта
    NOT_VISIBLE,          // Мод не отображается в игре
    NO_INTERNET,          // Нет интернета / сервер недоступен
    FILE_CORRUPTED,       // Файл повреждён
    INSTALL_MOD,          // Как установить мод
    INSTALL_TEXTURE,      // Как установить текстур-пак / скин
    NEED_INTERNET,        // Нужен ли интернет
    NEW_MODS,             // Будут ли новые моды
    NOT_WORKING,          // Куда писать, если не работает
    SHARE_MOD,            // Как поделиться своим модом
    MULTIPLAYER,          // Мультиплеер
    SAFE,                 // Безопасность модов
    LAG,                  // Почему лагает
    ADS                   // Почему есть реклама
}

data class FaqModelUi(
    @StringRes val questionRes: Int,
    @StringRes val answerRes: Int,
    val typeQuestion: FaqType
)

val faqList = listOf(
    FaqModelUi(
        R.string.faq_q_install_error,
        R.string.faq_a_install_error,
        FaqType.INSTALL_ERROR
    ),
    FaqModelUi(
        R.string.faq_q_not_visible,
        R.string.faq_a_not_visible,
        FaqType.NOT_VISIBLE
    ),
    FaqModelUi(
        R.string.faq_q_no_internet,
        R.string.faq_a_no_internet,
        FaqType.NO_INTERNET
    ),
    FaqModelUi(
        R.string.faq_q_file_corrupted,
        R.string.faq_a_file_corrupted,
        FaqType.FILE_CORRUPTED
    ),
    FaqModelUi(
        R.string.faq_q_install_mod,
        R.string.faq_a_install_mod,
        FaqType.INSTALL_MOD
    ),
    FaqModelUi(
        R.string.faq_q_install_texture,
        R.string.faq_a_install_texture,
        FaqType.INSTALL_TEXTURE
    ),
    FaqModelUi(
        R.string.faq_q_need_internet,
        R.string.faq_a_need_internet,
        FaqType.NEED_INTERNET
    ),
    FaqModelUi(
        R.string.faq_q_new_mods,
        R.string.faq_a_new_mods,
        FaqType.NEW_MODS
    ),
    FaqModelUi(
        R.string.faq_q_not_working,
        R.string.faq_a_not_working,
        FaqType.NOT_WORKING
    ),
    FaqModelUi(
        R.string.faq_q_share_mod,
        R.string.faq_a_share_mod,
        FaqType.SHARE_MOD
    ),
    FaqModelUi(
        R.string.faq_q_multiplayer,
        R.string.faq_a_multiplayer,
        FaqType.MULTIPLAYER
    ),
    FaqModelUi(
        R.string.faq_q_safe,
        R.string.faq_a_safe,
        FaqType.SAFE
    ),
    FaqModelUi(
        R.string.faq_q_lag,
        R.string.faq_a_lag,
        FaqType.LAG
    ),
    FaqModelUi(
        R.string.faq_q_ads,
        R.string.faq_a_ads,
        FaqType.ADS
    )
)