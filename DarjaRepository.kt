package com.digital.madrasa.data

import android.content.Context
import android.util.Log
import com.digital.madrasa.model.Book
import com.digital.madrasa.model.DarjaDetail
import com.google.gson.Gson
import com.google.gson.JsonObject

object DarjaRepository {

    private const val TAG = "DarjaRepository"

    // Accent colors (fallback for hardcoded data)
    private val colors = listOf(
        0xFFC9A84C.toInt(),
        0xFF10B981.toInt(),
        0xFF3B82F6.toInt(),
        0xFF8B5CF6.toInt(),
        0xFFF59E0B.toInt(),
        0xFFEC4899.toInt(),
        0xFF06B6D4.toInt(),
        0xFFEF4444.toInt()
    )

    // ── Main entry point ───────────────────────────────────────────────
    // Tries local JSON first, falls back to hardcoded data
    fun getDarjaDetail(context: Context, darjaId: Int): DarjaDetail {
        return try {
            if (RemoteRepository.hasLocalData(context)) {
                val fromJson = getDarjaFromJson(context, darjaId)
                if (fromJson != null) {
                    Log.d(TAG, "Loaded Darja $darjaId from local JSON")
                    return fromJson
                }
            }
            Log.d(TAG, "Using hardcoded data for Darja $darjaId")
            getHardcodedDarja(darjaId)
        } catch (e: Exception) {
            Log.e(TAG, "Error loading Darja $darjaId, using hardcoded: ${e.message}")
            getHardcodedDarja(darjaId)
        }
    }

    // ── Overload without context (backward compatibility) ──────────────
    fun getDarjaDetail(darjaId: Int): DarjaDetail {
        return getHardcodedDarja(darjaId)
    }

    // ── Parse single darja from local JSON file ────────────────────────
    private fun getDarjaFromJson(context: Context, darjaId: Int): DarjaDetail? {
        return try {
            val file = RemoteRepository.getLocalFile(context)
            val jsonString = file.readText()
            val root = Gson().fromJson(jsonString, JsonObject::class.java)
            val darjas = root.getAsJsonObject("darjas") ?: return null
            val darjaObj = darjas.getAsJsonObject("darja_$darjaId") ?: return null

            val nameEn = darjaObj.get("name_en").asString
            val nameUr = darjaObj.get("name_ur").asString
            val colorStr = darjaObj.get("color").asString
            val color = java.lang.Long.decode(colorStr).toInt()

            val books = parseBooks(darjaObj, "books")
            val guideBooks = parseBooks(darjaObj, "guide_books")

            DarjaDetail(darjaId, nameEn, nameUr, color, books, guideBooks)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse Darja $darjaId from JSON: ${e.message}")
            null
        }
    }

    // ── Parse books array from a darja JSON object ─────────────────────
    private fun parseBooks(darjaObj: JsonObject, key: String): List<Book> {
        val books = mutableListOf<Book>()
        val arr = darjaObj.getAsJsonArray(key) ?: return books
        for (element in arr) {
            val obj = element.asJsonObject
            books.add(
                Book(
                    id = obj.get("id").asInt,
                    nameArabic = obj.get("name_ur").asString,
                    nameUrdu = obj.get("name_en").asString,
                    driveUrl = obj.get("url").asString,
                    fileSizeMb = obj.get("size").asFloat
                )
            )
        }
        return books
    }

    // ── Hardcoded fallback ─────────────────────────────────────────────
    private fun getHardcodedDarja(darjaId: Int): DarjaDetail {
        return when (darjaId) {
            1 -> getDarja1()
            2 -> getDarja2()
            3 -> getDarja3()
            4 -> getDarja4()
            5 -> getDarja5()
            6 -> getDarja6()
            7 -> getDarja7()
            8 -> getDarja8()
            else -> getDarja1()
        }
    }

    // ================================================================
    // DARJA 1 - درجہ اول
    // ================================================================
    private fun getDarja1(): DarjaDetail {
        val books = listOf(
            Book(1, "نحو میر", "Nahw Mir",
                "https://archive.org/download/DarsENizami_DarjaAula_1stYear_02/NahwMeerAlBushraColor.pdf", 5.2f),
            Book(2, "شرح مائة عامل مع التركيب", "Sharh Mia Amil",
                "https://archive.org/download/DarsENizami_DarjaAula_1stYear_02/SharhMiataAmilAlBushraColor.pdf", 8.1f),
            Book(3, "ميزان الصرف و منشعب", "Mizan ul Sarf",
                "https://archive.org/download/DarsENizami_DarjaAula_1stYear_02/MezanOManshaabAlBushraColor.pdf", 6.4f),
            Book(4, "ارشاد الصرف", "Irshad ul Sarf",
                "https://archive.org/download/DarsENizami_DarjaAula_1stYear/IrshadUsSarf.pdf", 4.8f),
            Book(5, "علم الصرف (تین حصص)", "Ilm ul Sarf (3 Parts)",
                "https://archive.org/download/DarsENizami_DarjaAula_1stYear_02/IlmUsSarfAowalainAlBushraColor.pdf", 12.3f),
            Book(6, "صفوة المصادر", "Safwat ul Masadir",
                "https://archive.org/download/ARABISAFWATULMASADIR/ARABI%20SAFWA%20TUL%20MASADIR.pdf", 3.9f),
            Book(7, "تيسير الابواب", "Tayseer ul Abwab",
                "https://ia601806.us.archive.org/34/items/20201202_20201202_1714/%D8%AA%DB%8C%D8%B3%DB%8C%D8%B1%20%D8%A7%D8%A8%D9%88%D8%A7%D8%A8%20%D8%A7%D9%84%D8%B5%D8%B1%D9%81.pdf", 5.5f),
            Book(8, "المنهاج فى القواعد والاعراب", "Al Minhaj fil Qawaid",
                "https://archive.org/download/Mnhaj_Qawaid_I3rab/Mnhaj_Qawaid_I3rab.pdf", 9.2f),
            Book(9, "النحو اليسير", "Al Nahw ul Yaseer",
                "https://archive.org/download/a1571n/a1571n.pdf", 7.1f),
            Book(10, "تسهيل النحو", "Tasheel ul Nahw",
                "https://archive.org/download/DarsENizami_DarjaAula_1stYear/TASHEEL%20UL%20NAHW.pdf", 6.8f),
            Book(11, " علم النحو", "Ilm ul Nahw",
                "https://archive.org/download/DarsENizami_DarjaAula_1stYear/AL%20TAREQA%20TUL%20ASRIYAH%20VOL%201.pdf", 15.4f),
            Book(12, "جوامع الكلم", "Jawami ul Kalim",
                "https://archive.org/download/jawami_al-kalim/%D8%AC%D9%88%D8%A7%D9%85%D8%B9%20%D8%A7%D9%84%D9%83%D9%84%D9%85%20=%20%DA%86%D9%87%D9%84%20%D8%AD%D8%AF%D9%8A%D8%AB%20%D9%85%D9%81%D8%AA%D9%8A%20%D9%85%D8%AD%D9%85%D8%AF%20%D8%B4%D9%81%D9%8A%D8%B9.pdf", 4.2f),
            Book(13, "جمال القرآن", "Jamal ul Quran",
                "https://archive.org/download/DarsENizami_DarjaAula_1stYear_02/JamalUlQuranAlBushraColor.pdf", 8.7f),
            Book(14, "الطريقة العصريه جلد اول", "Tareeq E Asriya 1",
                "https://archive.org/download/DarsENizami_DarjaAula_1stYear/AL%20TAREQA%20TUL%20ASRIYAH%20VOL%201.pdf", 5.1f),
            Book(15, "الطريقة العصريه جلد دوم", "Tayseer ul Mubtadi",
                "https://archive.org/download/278651259/278651259-%D8%B9%D9%84%D9%85-%D8%A7%D9%84%D9%86%D8%AD%D9%88.pdf", 3.6f),
        )
        val guideBooks = listOf(
            Book(1, "شرح نحو میر", "Sharh Nahw Mir",
                "https://archive.org/download/DarsENizami_DarjaAula_1stYear/TanveerUrduSharhNahwmeer.pdf", 10.2f),
            Book(2, "شرح میزان الصرف", "Sharh Mizan ul Sarf",
                "https://archive.org/download/DarsENizami_DarjaAula_1stYear/MaarifUsSarfUrduSharhIrshadUsSarf.pdf", 8.5f),
            Book(3, "شرح ارشاد الصرف", "Sharh Irshad ul Sarf",
                "https://archive.org/download/DarsENizami_DarjaAula_1stYear/AnwaarUsSarfUrduSharhIrshadUsSarf.pdf", 7.3f),
            Book(4, "حاشیہ نحو میر", "Hashiya Nahw Mir",
                "https://archive.org/download/DarsENizami_DarjaAula_1stYear/BADR%20E%20MUNEER%20URDU%20SHAR%20NAHWMEER.pdf", 6.1f),
            Book(5, "شرح المنهاج", "Sharh Al Minhaj",
                "https://archive.org/download/20230311_20230311_1810/%D8%B4%D8%B1%D8%AD%20%D8%A7%D9%84%D9%85%D9%86%D9%87%D8%A7%D8%AC%20%D9%81%D9%8A%20%D8%A7%D9%84%D9%81%D9%82%D9%87%20%D8%AF%D8%A7%D8%B1_%D8%A7%D9%84%D8%B6%D9%8A%D8%A7%D8%A1/%D8%B4%D8%B1%D8%AD_%D8%A7%D9%84%D9%85%D9%86%D9%87%D8%A7%D8%AC_%D9%81%D9%8A_%D8%A7%D9%84%D9%81%D9%82%D9%87_%D8%AC_%D9%A1_%D8%AF%D8%A7%D8%B1_%D8%A7%D9%84%D8%B6%D9%8A%D8%A7%D8%A1.pdf", 9.4f)
        )
        return DarjaDetail(1, "Darja Aula", "درجہ اول", colors[0], books, guideBooks)
    }

    // ================================================================
    // DARJA 2 - درجہ ثانیہ
    // ================================================================
    private fun getDarja2(): DarjaDetail {
        val books = listOf(
            Book(1, "فوائد مكيه (تجويد)", "Fawaid Makkiya",
                "https://archive.org/download/DarsENizamiDarjaSaniah2ndYear1/FawaidEMakkiyahAlBushraColor.pdf", 5.8f),
            Book(2, "القراءة الراشده (جلد اول)", "Al Qiraah Al Rashida",
                "https://archive.org/download/Httpsarchive.orgdetailsDarsENizamiDarjaSaniah2ndYear2/AL_QIRAAT_AL_RASHIDAH.pdf", 7.2f),
            Book(3, "معلم الانشاء (جلد اول)", "Moallim ul Insha",
                "https://archive.org/download/DarsENizamiDarjaSaniah2ndYear1/MuallimUlInshaVol1.pdf", 8.9f),
            Book(4, "زاد الطالبين (کامل)", "Zad ul Talibeen",
                "https://archive.org/download/Httpsarchive.orgdetailsDarsENizamiDarjaSaniah2ndYear2/ZadUlTalibeenAlBushraColor.pdf", 12.1f),
            Book(5, "مختصر القدورى", "Mukhtasar ul Quduri",
                "https://archive.org/download/Httpsarchive.orgdetailsDarsENizamiDarjaSaniah2ndYear2/MukhtasarUlQudoriAlBushraColor.pdf", 9.4f),
            Book(6, "علم الصيغه مع خاصيات ابواب", "Ilm ul Seegh",
                "https://archive.org/download/Httpsarchive.orgdetailsDarsENizamiDarjaSaniah2ndYear2/IlmUsSeghaAlBushraColor.pdf", 6.7f),
            Book(7, "بداية النحو (کامل)", "Bidayat ul Nahw",
                "https://archive.org/download/Httpsarchive.orgdetailsDarsENizamiDarjaSaniah2ndYear2/HidayaTunNahwAlBushraColor.pdf", 11.3f),
            Book(8, "تيسير المنطق", "Tayseer ul Mantiq",
                "https://archive.org/download/Httpsarchive.orgdetailsDarsENizamiDarjaSaniah2ndYear2/TaiseerUlMantiqAlBushraColor.pdf", 5.6f),
            Book(9, "مرقاة", "Mirqat",
                "https://archive.org/download/Httpsarchive.orgdetailsDarsENizamiDarjaSaniah2ndYear2/AlMirqatAlBushraColor.pdf", 4.3f),
        )
        val guideBooks = listOf(
            Book(1, "شرح مختصر القدوری", "Sharh Mukhtasar Quduri",
                "https://archive.org/download/Httpsarchive.orgdetailsDarsENizamiDarjaSaniah2ndYear2/MukhtasarUlQudoriAlBushraColor.pdf", 14.2f),
            Book(2, "شرح بدایۃ النحو", "Sharh Bidayat ul Nahw",
                "https://archive.org/download/DarsENizamiDarjaSaniah2ndYear1/SaayatUnNahwUrduSharhHidayatUnNahw.pdf", 9.1f),
            Book(3, "شرح تیسیر المنطق", "Sharh Tayseer ul Mantiq",
                "https://archive.org/download/amst0/ASAAN_MANTIQ_SHARH_TAISIR_E_MANTIQ.pdf", 7.5f),
            Book(4, "حاشیہ مرقاة", "Hashiya Mirqat",
                "https://archive.org/download/DarsENizamiDarjaSaniah2ndYear1/TozihaatUrduSharhMirqat.pdf", 5.8f)
        )
        return DarjaDetail(2, "Darja Saniya", "درجہ ثانیہ", colors[1], books, guideBooks)
    }

    // ================================================================
    // DARJA 3 - درجہ ثالثہ
    // ================================================================
    private fun getDarja3(): DarjaDetail {
        val books = listOf(
            Book(1, "رياز الصالحين", "Riyadh ul Saliheen",
                "https://archive.org/download/RiazusSalihin/Riazus%20Salihin%20%28AlBushra%29%20By%20Imam%20Navavi.pdf", 18.4f),
            Book(2, "مختصر القدورى", "Mukhtasar ul Quduri",
                "https://archive.org/download/Httpsarchive.orgdetailsDarsENizamiDarjaSaniah2ndYear2/MukhtasarUlQudoriAlBushraColor.pdf", 9.4f),
            Book(3, "اصول الشاشى", "Usool ul Shashi",
                "https://archive.org/download/DarsENizamiDarjaSalsa3rdYear/UsoolUlShashiAlBushraColor.pdf", 6.2f),
            Book(4, "مبادى الاصول", "Mabadi ul Usool",
                "https://archive.org/download/DarsENizamiDarjaSalsa3rdYear/MABADI_AL_USOOL.pdf", 4.8f),
            Book(5, "کافيه", "Kafiya",
                "https://archive.org/download/DarsENizamiDarjaSalsa3rdYear/KafiahAlBushraColor.pdf", 7.9f),
            Book(6, "شرح تهذيب", "Sharh Tahzib",
                "https://archive.org/download/DarsENizamiDarjaSalsa3rdYear/SharhTahzeebAlBushraColor.pdf", 11.2f),
            Book(7, "متن عقيدة الطحاويه", "Matn Aqida Tahawiya",
                "https://archive.org/download/DarjaAlSadisa6thYear/MatanUlAqedaTitTahavi-AlBushraColo.pdf", 3.5f),
            Book(8, "نفحة العرب", "Nafhat ul Arab",
                "https://archive.org/download/DarsENizamiDarjaSalsa3rdYear/NafhaTulArabAlBushraColor.pdf", 8.6f),
            Book(9, "معلم الانشاء (جز ثانى)", "Moallim ul Insha Part 2",
                "https://archive.org/download/DarsENizamiDarjaSalsa3rdYear/AshrafUlInshaUrduSharhMuallimUlInsha2.pdf", 9.3f)
        )
        val guideBooks = listOf(
            Book(1, "شرح اصول الشاشی", "Sharh Usool ul Shashi",
                "https://archive.org/download/DarsENizamiDarjaSalsa3rdYear/AjmalUlHawashiUrduSharhUsoolUshShashi.pdf", 12.5f),
            Book(2, "شرح کافیہ", "Sharh Kafiya",
                "https://archive.org/download/darsenizamidarjasalsa3rdyear2/WAFIA_SHARH_KAFIA.pdf", 14.8f),
            Book(3, "شرح عقیدہ طحاویہ", "Sharh Aqida Tahawiya",
                "https://archive.org/download/sharah-aqeedah-tahaviya-urdu-aur-ibn-e-abi-aleez-pr-aik-tahqeeqi-nazar/SHARAH%20AQEEDAH%20TAHAVIYA%20URDU%20-%20AUR%20%20-%20IBN%20E%20ABI%20AL%20EEZ%20PR%20AIK%20TAHQEEQI%20NAZAR.pdf", 8.9f),
            Book(4, "حاشیہ شرح تہذیب", "Hashiya Sharh Tahzib",
                "https://archive.org/download/TanveerUlTahzeebSharahSharahTahzeeb/Tanveer%20ul%20Tahzeeb%20Sharah%20Sharah%20Tahzeeb%20.pdf", 10.2f)
        )
        return DarjaDetail(3, "Darja Salisa", "درجہ ثالثہ", colors[2], books, guideBooks)
    }

    // ================================================================
    // DARJA 4 - درجہ رابعہ
    // ================================================================
    private fun getDarja4(): DarjaDetail {
        val books = listOf(
            Book(1, "رياز الصالحين", "Riyadh ul Saliheen",
                "https://archive.org/download/RiazusSalihin/Riazus%20Salihin%20%28AlBushra%29%20By%20Imam%20Navavi.pdf", 18.4f),
            Book(2, "كنز الدقائق", "Kanz ul Daqaiq",
                "https://archive.org/download/DarsENizamiDarjaSalsa3rdYear/KanzUlDaqaiqVol1AlBushraColor.pdf", 13.7f),
            Book(3, "نور الانوار", "Noor ul Anwar",
                "https://archive.org/download/NOORULANWAAR/NOOR%20UL%20ANWAAR%20RAHMANIA.pdf", 10.4f),
            Book(4, "شرح جامى", "Sharh Jami",
                "https://archive.org/download/SHARHJAMI/SharhMullaJamiAlBushraColor.pdf", 8.9f),
            Book(5, "مقامات حريرى (دس مقامے)", "Maqamat Hariri",
                "https://archive.org/download/DarsENizamiDarjaRabea4thYear/AlMaqamatUlHareriyahAlBushraColor.pdf", 11.2f),
            Book(6, "معلم الانشاء (جلد ثالث)", "Moallim ul Insha Part 3",
                "https://archive.org/download/DarsENizamiDarjaRabea4thYear/MuallimUlInshaVol3.pdf", 9.8f),
            Book(7, "دروس البلاغه", "Durus ul Balagha",
                "https://archive.org/download/DarsENizamiDarjaRabea4thYear/DurosUlBalaghaAlBushraColor.pdf", 7.6f),
            Book(8, "البلاغة الواضحه", "Al Balagha Al Waziha",
                "https://archive.org/download/Heliopolis1957_gmail_20180525_0206/%D8%A7%D9%84%D8%A8%D9%84%D8%A7%D8%BA%D8%A9%20%D8%A7%D9%84%D9%88%D8%A7%D8%B6%D8%AD%D8%A9.pdf", 8.3f),
            Book(9, "قطبى", "Qutbi",
                "https://archive.org/download/DarsENizamiDarjaRabea4thYear/AlQutbiAlBushraColor.pdf", 6.1f)
        )
        val guideBooks = listOf(
            Book(1, "شرح کنز الدقائق", "Sharh Kanz ul Daqaiq",
                "https://archive.org/download/DarsENizamiDarjaSalsa3rdYear/TAYSIR_UL_HAQAIQ_ARABIC_SHARH_KANZ.pdf", 18.9f),
            Book(2, "شرح نور الانوار", "Sharh Noor ul Anwar",
                "https://archive.org/download/DarsENizamiDarjaRabea4thYear/KhulasatUlAnwarUrduSharhNoorUlAnwar.pdf", 12.4f),
            Book(3, "شرح جامی کا حاشیہ", "Hashiya Sharh Jami",
                "https://archive.org/download/DarsENizamiDarjaRabea4thYear/KhulasatUlJamiUrduSharhSharhUlJami.pdf", 9.7f),
            Book(4, "شرح قطبی", "Sharh Qutbi",
                "https://archive.org/download/DarsENizamiDarjaRabea4thYear/KHULAS_E_QUTBI.pdf", 7.8f),
            Book(5, "شرح دروس البلاغہ", "Sharh Durus ul Balagha",
                "https://archive.org/download/DarsENizamiDarjaRabea4thYear/MiftahUlBalaghaUrduSharhDuroosUlBalagha.pdf", 10.1f)
        )
        return DarjaDetail(4, "Darja Rabia", "درجہ رابعہ", colors[3], books, guideBooks)
    }

    // ================================================================
    // DARJA 5 - درجہ خامسہ
    // ================================================================
    private fun getDarja5(): DarjaDetail {
        val books = listOf(
            Book(1, "آثار السنن", "Asar ul Sunan",
                "https://archive.org/download/DarsENizamiDarjaKhamsa5thYear/AasaarUsSunanAlBushraColor.pdf", 14.2f),
            Book(2, "بدايه (جلد اول)", "Bidaya (Vol 1)",
                "https://archive.org/download/DarsENizamiDarjaKhamsa5thYear/AlHidayahVol1AlBushraColor.pdf", 19.8f),
            Book(3, "حسامى", "Husami",
                "https://archive.org/download/DarsENizamiDarjaKhamsa5thYear/MuntakhabUlHusamiAlBushraColor.pdf", 8.4f),
            Book(4, "نور الانوار (باپ قياس سے آخر تک)", "Noor ul Anwar (Qiyas)",
                "https://archive.org/download/NOORULANWAAR/NOOR%20UL%20ANWAAR%20RAHMANIA.pdf", 6.9f),
            Book(5, "مختصر المعانى (مکمل)", "Mukhtasar ul Maani",
                "https://archive.org/download/DarsENizamiDarjaKhamsa5thYear/MukhtasarUlMaaniVol1AlBushraColor.pdf", 12.7f),
            Book(6, "الانتبابات المفيده", "Al Intibabat ul Mufida",
                "https://archive.org/download/DarsENizamiDarjaKhamsa5thYear/AlIntebahaatUlMufedahAlBushraColor.pdf", 5.3f),
            Book(7, "معين الفلسفه", "Moin ul Falsafa",
                "https://archive.org/download/DarsENizamiDarjaKhamsa5thYear/MueenUlFalsafaAlBushraColor.pdf", 7.6f),
            Book(8, "شرح عقيدة الطحاويه (للعلامه البابرتى)", "Sharh Aqida Tahawiya",
                "https://archive.org/download/20210728_20210728_1916/%D8%B4%D8%B1%D8%AD%20%D8%A7%D9%84%D8%B9%D9%82%D9%8A%D8%AF%D8%A9%20%D8%A7%D9%84%D8%B7%D8%AD%D8%A7%D9%88%D9%8A%D8%A9%20-%20%D8%A7%D9%84%D8%A8%D8%A7%D8%A8%D8%B1%D8%AA%D9%8A%20-%20%D8%AA%20%D8%B9%D8%A8%D8%AF%20%D8%A7%D9%84%D8%B3%D9%84%D8%A7%D9%85%20%D8%B9%D8%A8%D8%AF%20%D8%A7%D9%84%D9%87%D8%A7%D8%AF%D9%8A%20%D8%B4%D9%86%D8%A7%D8%B1%20-%20%D8%AF%D8%A7%D8%B1%20%D8%A7%D9%84%D8%A8%D9%8A%D8%B1%D9%88%D8%AA%D9%8A.pdf", 16.4f),
            Book(9, "ديوان متنبى", "Diwan Mutanabbi",
                "https://archive.org/download/DEWANULMUTANABBI/DEWAN%20UL%20MUTANABBI%20MEER%20MUHAMMAD%20-%20Unknown.pdf", 9.1f),
            Book(10, "سبعه معلقات", "Saba Muallaqat",
                "https://archive.org/download/DarsENizamiDarjaKhamsa5thYear/AlMuallaqatUlSabaAlBushraColor.pdf", 7.3f)
        )
        val guideBooks = listOf(
            Book(1, "شرح آثار السنن", "Sharh Asar ul Sunan",
                "https://archive.org/download/DarsENizamiDarjaKhamsa5thYear/DarsEAasarUsSunanUrduSharhAasarUsSunan.pdf", 16.8f),
            Book(2, "شرح بدایہ", "Sharh Bidaya",
                "https://archive.org/download/DarsENizamiDarjaKhamsa5thYear/AhsanUlHidayaUrduSharhAlHidaya1.pdf", 22.4f),
            Book(3, "شرح مختصر المعانی", "Sharh Mukhtasar ul Maani",
                "https://archive.org/download/121.20/KHULASA_E_MUKHTASAR_AL_MAANI.pdf", 14.5f),
            Book(4, "شرح حسامی", "Sharh Husami",
                "https://archive.org/download/DarsENizamiDarjaKhamsa5thYear/AL-FAIZ-UL-HIJAZ-URDU-SHARH-AL-HUSAMI.pdf", 9.2f)
        )
        return DarjaDetail(5, "Darja Khamisa", "درجہ خامسہ", colors[4], books, guideBooks)
    }

    // ================================================================
    // DARJA 6 - درجہ سادسہ
    // ================================================================
    private fun getDarja6(): DarjaDetail {
        val books = listOf(
            Book(1, "تفسير جلالين (مکمل)", "Tafseer Jalalayn",
                "https://archive.org/download/AlSadisah6thYear1/TafseerUlJalalainVol1AlBushraColor.pdf", 28.6f),
            Book(2, "الفوز الكبير", "Al Fawz ul Kabeer",
                "https://archive.org/download/AlSadisah6thYear1/AlFaozUlKabeerAlBushraColor.pdf", 8.4f),
            Book(3, "خير الاصول", "Khair ul Usool",
                "https://archive.org/download/AlSadisah6thYear1/KHAIR_UL_USOOL.pdf", 7.2f),
            Book(4, "سراجى (فى الميراث)", "Siraji (Fil Mirath)",
                "https://archive.org/download/AlSadisah6thYear1/AlSirajiFilMerasAlBushraColor.pdf", 5.8f),
            Book(5, "كتاب الآثار (امام محمد)", "Kitab ul Asar",
                "https://archive.org/download/athar_alshibany/athar_alshibany.pdf", 12.3f),
            Book(6, "تسهيل الميراث", "Tasheel ul Mirath",
                "https://archive.org/download/20231026_20231026_1245/%D8%AA%D8%B3%D9%87%D9%8A%D9%84%20%D8%A7%D9%84%D9%81%D8%B1%D8%A7%D8%A6%D8%B6%20-%20%D8%A7%D8%A8%D9%86%20%D8%B9%D8%AB%D9%8A%D9%85%D9%8A%D9%86.pdf", 6.7f),
            Book(7, "بدايه (جلد ثانى)", "Bidaya (Vol 2)",
                "https://archive.org/download/AlSadisah6thYear1/AlHidayahVol3AlBushraColor.pdf", 21.4f),
            Book(8, "التوضيح والتلويح", "Al Tawzeeh wal Talweeh",
                "https://archive.org/download/AlTaozeehWatTalweeh/AL%20TAOZEEH%20WAT%20TALWEEH%20VOL%201-%20QADEMI.pdf", 17.9f),
            Book(9, "شرح عقائد النسفيه", "Sharh Aqaid Nasafiya",
                "https://archive.org/download/AlSadisah6thYear1/SHARHUL_AQAID_COLOR.pdf", 9.6f),
            Book(10, "علماء ديوبند کا دينى رخ اور مسلکى مزاج", "Ulama Deoband Ka Dini Rukh",
                "https://archive.org/download/20231128_20231128_0941/%D8%B9%D9%84%D9%85%D8%A7%D8%A1%D9%90_%D8%AF%DB%8C%D9%88%D8%A8%D9%86%D8%AF_%D8%A7%D9%88_%D8%A7%D9%86_%DA%A9%D8%A7_%D9%85%D8%B3%D9%84%DA%A9%DB%8C_%D9%85%D8%B2%D8%A7%D8%AC.pdf", 11.2f),
            Book(11, "درس فلکيات", "Dars Falakiyat",
                "https://archive.org/download/DarjaAlSadisa6thYear/FahmUlFalkiat-DarUlUloom.pdf", 8.1f),
            Book(12, "ديوان الحماسه", "Diwan ul Hamasa",
                "https://archive.org/download/AlSadisah6thYear1/DewanUlHamasahAlBushraColor.pdf", 14.3f),
            Book(13, "متن الکافى", "Matn ul Kafi",
                "https://archive.org/download/AlSadisah6thYear1/MatanUlKafiAlBushraColor.pdf", 7.8f)
        )
        val guideBooks = listOf(
            Book(1, "شرح تفسیر جلالین", "Sharh Tafseer Jalalayn",
                "https://archive.org/download/AlSadisah6thYear1/TafseerEJamalainVol01.pdf", 32.5f),
            Book(2, "شرح التوضیح والتلویح", "Sharh Tawzeeh wal Talweeh",
                "https://archive.org/download/DarjaAlSadisa6thYear/AlTarweehUrduSharhAlTaozeehWatTalweeh.pdf", 20.1f),
            Book(3, "شرح عقائد نسفیہ کا حاشیہ", "Hashiya Sharh Aqaid Nasafiya",
                "https://archive.org/download/DarjaAlSadisa6thYear/TaozeehUlAqaidUrduSharhSharhUlAqaid.pdf", 11.4f),
            Book(4, "شرح کتاب الآثار", "Sharh Kitab ul Asar",
                "https://archive.org/download/TOOBAA-roza-tul-azhaar-SHARAH-KITAB-UL-AASAAR-URDU/roza%20tul%20azhaar1-1.pdf", 14.7f),
            Book(5, "شرح سراجی", "Sharh Siraji",
                "https://archive.org/download/FahmEMiraasKeAsanRahen/Fahm_e_Miraas_ke_Asan_Rahen.pdf", 8.3f)
        )
        return DarjaDetail(6, "Darja Sadisa", "درجہ سادسہ", colors[5], books, guideBooks)
    }

    // ================================================================
    // DARJA 7 - درجہ سابعہ
    // ================================================================
    private fun getDarja7(): DarjaDetail {
        val books = listOf(
            Book(1, "التبيان فى علوم القرآن", "Al Tibyan fi Uloom ul Quran",
                "https://archive.org/download/DarsENizamiDarjaSabeaaMaoqoofAlai7thYear/AlTibyanAlBushraColor.pdf", 16.8f),
            Book(2, "تيسير مصطلح الحديث", "Tayseer Mustalah ul Hadith",
                "https://archive.org/download/DarsENizamiDarjaSabeaaMaoqoofAlai7thYear01/TaiseerEMustalahUlHadithAlBushraColor.pdf", 9.4f),
            Book(3, "شرح نخبة الفكر", "Sharh Nukhbat ul Fikr",
                "https://archive.org/download/DarsENizamiDarjaSabeaaMaoqoofAlai7thYear01/SharahNukhbaTulFikrAlBushraColor.pdf", 11.2f),
            Book(4, "آئينه قاديانيت", "Aaina Qadiyaniyat",
                "https://archive.org/download/Aaina-e-Qadiyaniat-109751/Aaina-e-Qadiyaniat-109751-WQB.pdf", 7.6f),
            Book(5, "تفسير بيضاوى (ربع پاره اول)", "Tafseer Baydawi",
                "https://archive.org/download/DarsENizamiDarjaSabeaaMaoqoofAlai7thYear/TafseerUlBaizawiAlBushraColor.pdf", 56.07f),
            Book(6, "مشکوٰة المصابيح (جلد اول)", "Mishkat ul Masabih Vol 1",
                "https://archive.org/download/DarsENizamiDarjaSabeaaMaoqoofAlai7thYear/MishkatUlMasabeehVol1AlBushraColor.pdf", 24.7f),
            Book(7, "مشکوٰة المصابيح (جلد دوم)", "Mishkat ul Masabih Vol 2",
                "https://archive.org/download/DarsENizamiDarjaSabeaaMaoqoofAlai7thYear/MishkatUlMasabeehVol2AlBushraColor.pdf", 23.9f),
            Book(8, "بدايه (جلد ثالث)", "Bidaya (Vol 3)",
                "https://archive.org/download/DarsENizamiDarjaSabeaaMaoqoofAlai7thYear/AlHidayahVol5AlBushraColor.pdf", 20.4f),
            Book(9, "بدايه (جلد رابع)", "Bidaya (Vol 4)",
                "https://archive.org/download/DarsENizamiDarjaSabeaaMaoqoofAlai7thYear/AlHidayahVol6AlBushraColor.pdf", 19.8f)
        )
        val guideBooks = listOf(
            Book(1, "شرح مشکوٰۃ المصابیح", "Sharh Mishkat ul Masabih",
                "https://archive.org/download/DarsENizamiDarjaSabeaaMaoqoofAlai7thYear01/TOHFA_TUL_MIRAT_DUROOS_MISHKAT.pdf", 45.2f),
            Book(2, "شرح نخبۃ الفکر", "Sharh Nukhbat ul Fikr",
                "https://archive.org/download/DarsENizamiDarjaSabeaaMaoqoofAlai7thYear/UmdatUnNazarUrduSharhNuhbatUlFikar.pdf", 13.8f),
            Book(3, "شرح تیسیر مصطلح الحدیث", "Sharh Tayseer Mustalah",
                "https://archive.org/download/www.besturdubooks.net/ASAN-ISTELAHAAT-E-HADEES.pdf", 10.5f),
            Book(4, "شرح تفسیر بیضاوی", "Sharh Tafseer Baydawi",
                "https://archive.org/download/DarsENizamiDarjaSabeaaMaoqoofAlai7thYear/AlTaqreerUlHaviUrduSharhTafseerUlBaizavi.pdf", 28.4f)
        )
        return DarjaDetail(7, "Darja Sabia", "درجہ سابعہ", colors[6], books, guideBooks)
    }

    // ================================================================
    // DARJA 8 - دورہ حدیث
    // ================================================================
    private fun getDarja8(): DarjaDetail {
        val books = listOf(
            Book(1, "صحيح بخارى", "Sahih Bukhari",
                "https://archive.org/download/DarsENizamiDoraeHadees8thYear01/SAHIH_AL_BUKHARI_BUSHRA_01.pdf", 68.4f),
            Book(2, "صحيح مسلم", "Sahih Muslim",
                "https://archive.org/download/DarsENizamiDoraEHadees8thYear/AL-SAHI-LI-MUSLIM-VOL-01-AL-BUSHRA-COLOR.pdf", 52.7f),
            Book(3, "جامع الترمذى", "Jami ul Tirmizi",
                "https://archive.org/download/DarsENizamiDoraEHadees8thYear03/JAMI_AL_TIRMIZI_BUSHRA_VOL_01.pdf", 41.3f),
            Book(4, "سنن ابى داؤد", "Sunan Abu Dawud",
                "https://archive.org/download/DarsENizamiDoraEHadees8thYear/SUNAN-E-ABI-DAWUD-MEER-MUHAMMAD.pdf", 38.9f),
            Book(5, "سنن نسائى", "Sunan Nasai",
                "https://archive.org/download/DarsENizamiDoraeHadees8thYear01/SUNAN_AL_NASAI_RAHMANIA_VOL_01.pdf", 36.2f),
            Book(6, "سنن ابن ماجه", "Sunan Ibn Majah",
                "https://archive.org/download/DarsENizamiDoraEHadees8thYear/SUNAN-E-IBN-E-MAJA-RAHMANIA.pdf", 33.8f),
            Book(7, "شمائل ترمذى (مکمل)", "Shamail Tirmizi",
                "https://archive.org/download/DarsENizamiDoraEHadees8thYear/SHAMAIL-E-TIRMEZI-AL-BUSHRA-COLOR.pdf", 18.4f),
            Book(8, "مؤطا امام مالک", "Muwatta Imam Malik",
                "https://archive.org/download/DarsENizamiDoraeHadees8thYear01/MUATT_IMAM_MALIK_RAHMANIA.pdf", 29.6f),
            Book(9, "مؤطا امام محمد", "Wattage Imam Muhammad",
                "https://archive.org/download/DarsENizamiDoraEHadees8thYear/AlMuattaLilImamMuhammadVol1AlBushraColor.pdf", 27.3f),
            Book(10, "سنن طحاوى", "Sunan Tahawe",
                "https://archive.org/download/DarsENizamiDoraEHadees8thYear/SHARH-MAANI-UL-AASAAR-VOL-1-RAHMANIA.pdf", 44.1f)
        )
        val guideBooks = listOf(
            Book(1, "فتح الباری شرح صحیح بخاری", "Fath ul Bari",
                "https://archive.org/download/8_20200602_202006/%D9%81%D8%AA%D8%AD%20%D8%A7%D9%84%D8%A8%D8%A7%D8%B1%DB%8C%20%D8%B4%D8%B1%D8%AD%20%D8%B5%D8%AD%DB%8C%D8%AD%20%D8%A8%D8%AE%D8%A7%D8%B1%DB%8C%20%281%29.pdf", 125.4f),
            Book(2, "شرح صحیح مسلم للنووی", "Sharh Sahih Muslim Nawawi",
                "https://archive.org/download/DarsENizamiDoraEHadees8thYear03/TOHFATUL_MUNIM_SHARH_SAHIH_MUSLIM_01.pdf", 98.7f),
            Book(3, "عارضۃ الاحوذی شرح ترمذی", "Ariza ul Ahwazi",
                "https://archive.org/download/aarza-tul-hawzi-bisharah-sahi-al-tirmizi/02_35432.pdf", 76.2f),
            Book(4, "بذل المجہود شرح ابی داؤد", "Bazl ul Majhood",
                "https://archive.org/download/BazlulMajhoodNadwi/BathlMAjhood_1.pdf", 82.5f),
            Book(5, "انجاز الحاجۃ شرح ابن ماجہ", "Injaz ul Haja",
                "https://archive.org/download/enjazalhajah1/EnjazAlhajah-1.pdf", 68.9f),
            Book(6, "اوجز المسالک شرح مؤطا مالک", "Awjaz ul Masalik",
                "https://archive.org/download/ahmedmadi81_gmail_09/01.pdf", 91.3f)
        )
        return DarjaDetail(8, "Dawra-e-Hadith", "دورہ حدیث", colors[7], books, guideBooks)
    }
}
