# Kriptografi-G-Fonksiyonu-Kriptografik-Rastgele-Say-reteci
# Kriptografik Rastgele Sayı Üreteci (G Fonksiyonu)

Bu proje, Collatz Sanısı mantığını temel alan, Bernoulli ve Riccati tipi dinamik dönüşümlerle güçlendirilmiş bir **G-Fonksiyonu** tasarımıdır.

## 1. Algoritma Mantığı
Bu üreteç, Kerckhoffs ilkesine uygun olarak tasarlanmıştır. Algoritmanın tüm detayları bilinse dahi, anahtarın gizliliği başlangıçtaki **Seed (Tohum)** değerine bağlıdır.

- **Dönüşüm:** Collatz sanısındaki çift/tek ayrımı kullanılarak; sayılar çift ise Bernoulli, tek ise Riccati tipi ayrık dönüşüm formüllerinden geçer.
- **Karıştırma (Mixing):** Patern oluşumunu engellemek için XOR-Shift (13, 7, 17) bit manipülasyonu uygulanmıştır. Bu, sistemde "Çığ Etkisi" yaratarak rastgeleliği maksimize eder.

## 2. Sözde Kod (Pseudo-Code)
1. Girdi olarak `seed` ve `n_bits` (anahtar uzunluğu) alınır.
2. Başlangıç durumu: `x = seed`.
3. Her adımda:
   - `x` çift ise: `x = a * x + b` (Bernoulli)
   - `x` tek ise: `x = x^2 + c*x + d` (Riccati)
   - `x` değeri XOR-Shift işlemlerinden geçirilir.
   - Sonuç bit dizisine `x % 2` eklenir.
4. Çıktı olarak bit dizisi döndürülür.

## 3. İstatistiksel Kalite Testleri
Algoritma, rastgeleliğin doğrulanması için aşağıdaki testlerden geçirilmiştir:
- **Monobit Testi (0/1 Dengesi):** Üretilen 100.000 bitlik dizide 0 ve 1 sayılarının yaklaşık %50 oranında dağıldığı gözlemlenmiştir.
- **Runs Test:** Ardışık bit değişimleri kontrol edilerek patern oluşmadığı doğrulanmıştır.
<img width="217" height="537" alt="image" src="https://github.com/user-attachments/assets/2f656356-e2a6-4b10-81b7-23000fc724ce" />

