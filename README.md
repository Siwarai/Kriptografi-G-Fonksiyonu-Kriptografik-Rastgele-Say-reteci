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
<img width="541" height="666" alt="image" src="https://github.com/user-attachments/assets/e6bc8722-f46b-4ed1-b859-a96b164b1046" />
<img width="416" height="607" alt="image" src="https://github.com/user-attachments/assets/a333d67e-8e69-4edd-bf44-3f312e88e225" />
<img width="476" height="632" alt="image" src="https://github.com/user-attachments/assets/29d6d413-844b-4351-88b2-6348e585d5a3" />
<img width="569" height="710" alt="image" src="https://github.com/user-attachments/assets/0c6ec431-121f-4a8a-ad42-954b45b16926" />
<img width="381" height="461" alt="image" src="https://github.com/user-attachments/assets/0f76be80-11e9-40ec-91fe-1c44961bee3a" />






