package com.example.tcdogrulama

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.tcdogrulama.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var kutular: List<EditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        kutular = listOf(
            binding.d1, binding.d2, binding.d3, binding.d4, binding.d5,
            binding.d6, binding.d7, binding.d8, binding.d9, binding.d10, binding.d11
        )

        kutular.forEachIndexed { index, kutu -> hazirlaOtomatikIlerleme(kutu, index) }

        binding.btnDogrula.setOnClickListener {
            val girilen = kutular.joinToString("") { it.text.toString() }
            val sonuc = tcKimlikNoGecerliMi(girilen)
            binding.tvSonuc.text = sonuc.mesaj
            binding.dotSonuc.visibility = View.VISIBLE
            binding.dotSonuc.backgroundTintList = ColorStateList.valueOf(
                if (sonuc.gecerli) getColor(R.color.success) else getColor(R.color.error)
            )
            binding.dotSonuc.setBackgroundResource(R.drawable.dot_indicator)
            binding.tvSonuc.setTextColor(
                if (sonuc.gecerli) getColor(R.color.success) else getColor(R.color.error)
            )
        }
    }

    /**
     * Her kutuya: tek hane girilince otomatik olarak bir sonrakine geçme,
     * boşken Backspace'e basılınca bir öncekine dönme ve 11 haneyi birden
     * yapıştırma (ör. panodan) desteği ekler.
     */
    private fun hazirlaOtomatikIlerleme(kutu: EditText, index: Int) {
        kutu.doAfterTextChanged { metin ->
            val deger = metin?.toString().orEmpty()
            if (deger.length > 1) {
                dagitYapistirilanRakamlari(deger, index)
                return@doAfterTextChanged
            }
            if (deger.length == 1 && index < kutular.lastIndex) {
                kutular[index + 1].requestFocus()
                kutular[index + 1].setSelection(kutular[index + 1].text?.length ?: 0)
            }
        }

        kutu.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN &&
                kutu.text.isNullOrEmpty() && index > 0
            ) {
                kutular[index - 1].requestFocus()
                kutular[index - 1].text?.clear()
            }
            false
        }
    }

    private fun dagitYapistirilanRakamlari(yapistirilan: String, baslangicIndex: Int) {
        val rakamlar = yapistirilan.filter { it.isDigit() }
        var i = baslangicIndex
        for (r in rakamlar) {
            if (i > kutular.lastIndex) break
            kutular[i].setText(r.toString())
            i++
        }
        val odakIndex = (baslangicIndex + rakamlar.length).coerceAtMost(kutular.lastIndex)
        kutular[odakIndex].requestFocus()
        kutular[odakIndex].setSelection(kutular[odakIndex].text?.length ?: 0)
    }

    /**
     * Bu fonksiyon YALNIZCA T.C. Kimlik Numarası'nın resmi kontrol
     * algoritmasına (Nüfus ve Vatandaşlık İşleri Genel Müdürlüğü'nün
     * yayımladığı formül) uyup uymadığını denetler.
     *
     * ÖNEMLİ: Bu, numaranın gerçekten bir kişiye ait olup olmadığını,
     * o kişinin kim olduğunu veya herhangi bir kişisel bilgisini
     * DOĞRULAMAZ. Uygulama hiçbir sunucuya bağlanmaz, hiçbir veritabanını
     * sorgulamaz — tamamen cihaz üzerinde çalışır.
     */
    private fun tcKimlikNoGecerliMi(no: String): Sonuc {
        if (no.length != 11) {
            return Sonuc(false, "Eksik: 11 hanenin tamamını girin.")
        }
        if (!no.all { it.isDigit() }) {
            return Sonuc(false, "Geçersiz: Sadece rakam içermelidir.")
        }
        if (no[0] == '0') {
            return Sonuc(false, "Geçersiz: İlk hane 0 olamaz.")
        }

        val d = no.map { it - '0' }

        val tekToplam = d[0] + d[2] + d[4] + d[6] + d[8]
        val ciftToplam = d[1] + d[3] + d[5] + d[7]

        val hesap10 = ((tekToplam * 7) - ciftToplam) % 10
        val d10Beklenen = if (hesap10 < 0) hesap10 + 10 else hesap10

        val ilk10Toplam = d.subList(0, 10).sum()
        val d11Beklenen = ilk10Toplam % 10

        return if (d[9] == d10Beklenen && d[10] == d11Beklenen) {
            Sonuc(true, "Geçerli format: kontrol haneleri algoritmayla uyuşuyor.")
        } else {
            Sonuc(false, "Geçersiz: kontrol haneleri algoritmayla uyuşmuyor.")
        }
    }

    private data class Sonuc(val gecerli: Boolean, val mesaj: String)
}
