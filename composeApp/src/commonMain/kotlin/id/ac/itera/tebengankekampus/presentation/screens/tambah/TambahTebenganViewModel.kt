package id.ac.itera.tebengankekampus.presentation.screens.tambah

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ac.itera.tebengankekampus.domain.model.Tebengan
import id.ac.itera.tebengankekampus.domain.model.TitikLokasi
import id.ac.itera.tebengankekampus.domain.usecase.TambahTebenganUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

data class TambahFormState(
    val namaPengemudi: String = "Yowen Daniel",
    val kontak: String = "0812-0000-0003",
    val asal: String = "",
    val tujuan: String = "Kampus ITERA - Gerbang Utama",
    val jadwal: String = "2026-02-12T07:00:00",
    val kursi: String = "3",
    val harga: String = "5000",
    val catatan: String = "",
    val tersimpan: Boolean = false,
    val pesanGalat: String? = null,
)

class TambahTebenganViewModel(
    private val tambahTebenganUseCase: TambahTebenganUseCase,
) : ViewModel() {

    private val _form = MutableStateFlow(TambahFormState())
    val form: StateFlow<TambahFormState> = _form

    fun ubahAsal(nilai: String) = _form.update { it.copy(asal = nilai) }
    fun ubahTujuan(nilai: String) = _form.update { it.copy(tujuan = nilai) }
    fun ubahJadwal(nilai: String) = _form.update { it.copy(jadwal = nilai) }
    fun ubahKursi(nilai: String) = _form.update { it.copy(kursi = nilai.filter(Char::isDigit)) }
    fun ubahHarga(nilai: String) = _form.update { it.copy(harga = nilai.filter(Char::isDigit)) }
    fun ubahCatatan(nilai: String) = _form.update { it.copy(catatan = nilai) }

    fun simpan() {
        val formulir = _form.value
        val kursi = formulir.kursi.toIntOrNull()
        when {
            formulir.asal.isBlank() ->
                _form.update { it.copy(pesanGalat = "Titik jemput wajib diisi.") }

            kursi == null || kursi <= 0 ->
                _form.update { it.copy(pesanGalat = "Jumlah kursi tidak valid.") }

            else -> viewModelScope.launch {
                tambahTebenganUseCase(
                    Tebengan(
                        id = "TBN-" + Random.nextInt(1000, 9999),
                        namaPengemudi = formulir.namaPengemudi,
                        kontakPengemudi = formulir.kontak,
                        asal = TitikLokasi(formulir.asal, -5.3588, 105.3148),
                        tujuan = TitikLokasi(formulir.tujuan, -5.3595, 105.3118),
                        jadwal = formulir.jadwal,
                        kursiTersedia = kursi,
                        hargaPerOrang = formulir.harga.toIntOrNull() ?: 0,
                        catatan = formulir.catatan,
                    ),
                )
                _form.update { it.copy(tersimpan = true, pesanGalat = null) }
            }
        }
    }
}
