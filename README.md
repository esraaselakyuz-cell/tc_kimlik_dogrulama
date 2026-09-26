# T.C. No Doğrulama (Android / Kotlin)

## Bu uygulama ne yapar, ne yapmaz

**Yapar:** Girilen 11 haneli sayının, T.C. Kimlik Numarası için kullanılan
resmi kontrol algoritmasına (hane bazlı toplam/mod formülü) uyup uymadığını
cihaz üzerinde, tamamen çevrimdışı kontrol eder.

**Yapmaz:** Bu numaranın gerçek bir kişiye ait olup olmadığını, o kişinin
adını, adresini veya başka bir bilgisini göstermez. Böyle bir bilgiye
yalnızca resmî kurumlar (örneğin e-Devlet, MERNİS) yasal yollarla erişebilir;
bu uygulama hiçbir sunucuya bağlanmaz, `INTERNET` iznini bile istemez.

## Neden .apk dosyası olarak gönderilmedi?

Bu ortamda Android SDK ve internet erişimi bulunmuyor, bu yüzden burada
doğrudan bir `.apk` derlenemiyor. Bunun yerine derlemeye hazır, tam bir
Android Studio projesi hazırlandı. Aşağıdaki adımlarla kendi bilgisayarınızda
birkaç dakikada `.apk`'ya çevirebilirsiniz.

## APK'ya çevirme — Yol A: Bilgisayara hiçbir şey kurmadan (önerilen)

Bu projede hazır bir GitHub Actions workflow'u var (`.github/workflows/build-apk.yml`).
Bulutta otomatik olarak `.apk` derler, siz sadece dosyayı indirirsiniz.

1. [github.com](https://github.com) üzerinde ücretsiz bir hesap açın (yoksa).
2. Yeni bir **repository** oluşturun (public veya private, fark etmez).
3. Bu klasördeki tüm dosyaları (bu README ve `.github` klasörü dahil) o
   repository'ye yükleyin — GitHub'ın web arayüzünden "Add file > Upload
   files" ile sürükle-bırak yapabilirsiniz, komut satırı gerekmez.
4. Yükleme bitince GitHub otomatik olarak derlemeyi başlatır. Üstteki
   **Actions** sekmesine girip "APK Derle" işleminin bitmesini bekleyin
   (birkaç dakika sürer).
5. İşlem bittiğinde sayfanın altındaki **Artifacts** bölümünden
   `tc-dogrulama-debug-apk` dosyasını indirin — içinden çıkan `.apk`
   dosyasını telefonunuza aktarıp kurabilirsiniz.

## APK'ya çevirme — Yol B: Android Studio ile (bilgisayarınızda)

1. [Android Studio](https://developer.android.com/studio) kurun (ücretsiz).
2. Bu klasörü (tc-dogrulama) bilgisayarınıza indirip açın: **File > Open**.
3. Gradle senkronizasyonunun bitmesini bekleyin (ilk açılışta internet
   gerekir, bağımlılıkları indirir).
4. Menüden **Build > Build Bundle(s) / APK(s) > Build APK(s)** seçin.
5. Derleme bitince çıkan bildirimden **locate** butonuna basarak
   `app/build/outputs/apk/debug/app-debug.apk` dosyasını bulun.
6. Bu `.apk` dosyasını Turkcell T40 cihazınıza (veya herhangi bir Android
   cihaza) aktarıp kurabilirsiniz. Cihazda "Bilinmeyen kaynaklardan yükleme"
   iznini açmanız gerekebilir.

Not: Turkcell T40, standart Android işletim sistemi çalıştıran bir cihaz
olduğu için özel bir uyarlama gerekmez. Proje minSdk 26 (Android 8.0) olarak
ayarlı; T40 dahil güncel cihazların hemen hepsi bunun üzerinde bir sürümle
gelir.

## Tasarım

- **Palet:** Bordo (#6B1E2A) zemin, bronz/altın (#C9A66B) vurgu, kağıt beji
  (#F6F3EC) arka plan, mürekkep lacivert (#1D2430) metin — resmî kimlik
  kartının kendi renk dilinden alındı, Türk bayrağının kırmızı-beyazı yerine
  bilinçli olarak bu palet seçildi.
- **Giriş alanı:** Tek bir metin kutusu yerine, resmî belgelerde görülen
  hane-hane basılı kutulara gönderme yapan 11 ayrı kutu kullanıldı; bir hane
  girilince otomatik sıradaki kutuya geçer, panodan 11 haneyi birden
  yapıştırmayı da destekler.
- **Simge:** Kimlik kartı silueti + çip bloğu + onay işareti, bordo zemin
  üzerine bronz ve kağıt rengiyle çizilmiş bir adaptive icon (vektör,
  `ic_launcher_foreground.xml`).

## Proje yapısı

```
tc-dogrulama/
├── settings.gradle
├── build.gradle
├── gradle.properties
└── app/
    ├── build.gradle
    ├── proguard-rules.pro
    └── src/main/
        ├── AndroidManifest.xml
        ├── java/com/example/tcdogrulama/MainActivity.kt
        └── res/
            ├── layout/activity_main.xml
            └── values/strings.xml
```
