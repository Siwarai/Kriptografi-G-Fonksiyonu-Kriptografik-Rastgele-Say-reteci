import java.util.*;
public class BSGOdev14 {
    



    // ---------------- G(seed) ----------------
    static class G {
        private long x;

        // Bernoulli / Riccati parametreleri (sabitler)
        private static final long a = 1103515245L; // Bernoulli için çarpan
        private static final long b = 12345L;      // Bernoulli için sabit
        private static final long c = 3L;          // Riccati için lineer katsayı
        private static final long d = 1L;          // Riccati için sabit

        G(long seed) {
            if (seed == 0) seed = 1;
            x = seed;
        }

        // Bernoulli + Riccati tabanı + kısa "mix"
        private void step() {
            // Parçalı dönüşüm (çift/tek ayrımı korunuyor)
            if ((x & 1L) == 0L) {
                x = a * x + b;                 // Bernoulli: x_{n+1} = a*x + b
            } else {
                x = x * x + c * x + d;         // Riccati:  x_{n+1} = x^2 + c*x + d
            }

            // XOR-Shift karıştırma
            x ^= (x << 13);
            x ^= (x >>> 7);
            x ^= (x << 17);

            if (x == 0) x = 1;
        }

        int nextBit() {
            step();
            return (int) (x & 1L);
        }

        String nextKeyBits(int nBits) {
            StringBuilder sb = new StringBuilder(nBits);
            for (int i = 0; i < nBits; i++) sb.append(nextBit());
            return sb.toString();
        }
    }

    // ---------------- İstatistik Testler ----------------
    static class Tests {
        long zeros = 0, ones = 0;
        long runs = 0;
        long[] pairs = new long[4]; // 00,01,10,11

        void feedBits(int[] bits) {
            if (bits.length == 0) return;

            int prev = bits[0];
            if (prev == 0) zeros++;
            else ones++;
            runs = 1;

            for (int i = 1; i < bits.length; i++) {
                int b = bits[i];
                if (b == 0) zeros++;
                else ones++;

                if (b != prev) runs++;

                int idx = (prev << 1) | b;
                pairs[idx]++;

                prev = b;
            }
        }

        double monobitOran1(int n) {
            return ones / (double) n;
        }

        // Chi-Square (df=1): (Z - E)^2/E + (O - E)^2/E  , E = n/2
        double chiSquare01(int n) {
            double e = n / 2.0;
            double dz = zeros - e;
            double do1 = ones - e;
            return (dz * dz) / e + (do1 * do1) / e;
        }

        // df=1 için p-value: p = P(ChiSquare >= x) = erfc(sqrt(x/2))
        double pValueChiSquareDf1(double chi2) {
            return erfc(Math.sqrt(chi2 / 2.0));
        }

        void printReport(int n) {
            double oran1 = monobitOran1(n);
            double chi2 = chiSquare01(n);
            double p = pValueChiSquareDf1(chi2);

            System.out.println("\n--- ISTATISTIK RAPORU ---");
            System.out.println("Toplam bit            : " + n);

            // Monobit Frekans Testi
            System.out.println("\n[1] Monobit Frekans Testi");
            System.out.println("0 sayisi              : " + zeros);
            System.out.println("1 sayisi              : " + ones);
            System.out.printf ("1 orani               : %.6f (ideal 0.500000)\n", oran1);

            // Chi-Square Testi
            System.out.println("\n[2] Chi-Square (χ²) Testi (df=1)");
            System.out.printf ("χ² degeri             : %.6f\n", chi2);
            System.out.printf ("p-value               : %.6f\n", p);
            System.out.println("Karar (p > 0.01?)      : " + (p > 0.01 ? "EVET (rastgelelik varsayimi destekleniyor)" : "HAYIR (supheli)"));

            // Patern/Desen Analizi için basit göstergeler
            System.out.println("\n[3] Patern (Desen) Analizi - Basit Gosterge");
            System.out.println("Runs (degisim sayisi)  : " + runs + " (ne kadar degisim var gosterir)");
            System.out.println("2-bit pairs (00,01,10,11): "
                    + pairs[0] + ", " + pairs[1] + ", " + pairs[2] + ", " + pairs[3]);
        }
    }

    // ---------------- Yardımcı Matematik (erf/erfc) ----------------
    // erf için klasik yaklaşık (Abramowitz-Stegun 7.1.26), ödev için yeterli doğrulukta.
    static double erf(double x) {
        // constants
        double a1 =  0.254829592;
        double a2 = -0.284496736;
        double a3 =  1.421413741;
        double a4 = -1.453152027;
        double a5 =  1.061405429;
        double p  =  0.3275911;

        int sign = (x < 0) ? -1 : 1;
        x = Math.abs(x);

        double t = 1.0 / (1.0 + p * x);
        double y = 1.0 - (((((a5 * t + a4) * t) + a3) * t + a2) * t + a1) * t * Math.exp(-x * x);

        return sign * y;
    }

    static double erfc(double x) {
        return 1.0 - erf(x);
    }

    // ---------------- Seed doğrulama tablosu ----------------
    static class ValidationRow {
        long seed;
        String first16;
        double oran0;
        double oran1;

        ValidationRow(long seed, String first16, double oran0, double oran1) {
            this.seed = seed;
            this.first16 = first16;
            this.oran0 = oran0;
            this.oran1 = oran1;
        }
    }

    static ValidationRow validateSeed(long seed, int testBits) {
        // İlk 16 bit
        G g1 = new G(seed);
        String first16 = g1.nextKeyBits(16);

        // 100000 bit (veya testBits) için 0/1 oranı
        G g2 = new G(seed);
        long sifir = 0, bir = 0;
        for (int i = 0; i < testBits; i++) {
            if (g2.nextBit() == 0) sifir++;
            else bir++;
        }
        double oran0 = sifir / (double) testBits;
        double oran1 = bir / (double) testBits;

        return new ValidationRow(seed, first16, oran0, oran1);
    }

    static void printValidationTable(ValidationRow[] rows, int testBits) {
        System.out.println("\n--- ORNEK CIKTILAR ve DOGRULAMA (Validation) ---");
        System.out.println("Test bit sayisi: " + testBits);
        System.out.println();
        System.out.println("| Seed (Tohum) | Uretilen Ilk 16 Bit | " + testBits + " Bitlik 0/1 Orani |");
        System.out.println("|---:|:---:|:---:|");
        for (ValidationRow r : rows) {
            System.out.printf("| %d | `%s` | %.4f / %.4f |\n", r.seed, r.first16, r.oran0, r.oran1);
        }
    }

    // ---------------- main ----------------
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Seed gir: ");
        long seed = sc.nextLong();

        System.out.print("Anahtar uzunlugu (bit) (ornegin 128/256): ");
        int keyLen = sc.nextInt();

        System.out.print("Test icin kac bit uretelim? (ornegin 100000): ");
        int testBits = sc.nextInt();

        // 1) Anahtar üret
        G g = new G(seed);
        String key = g.nextKeyBits(keyLen);
        System.out.println("\nKEY (" + keyLen + " bit):");
        System.out.println(key);

        // 2) Test için bit akışı üret
        int[] bits = new int[testBits];
        for (int i = 0; i < testBits; i++) bits[i] = g.nextBit();

        // 3) Testleri çalıştır
        Tests t = new Tests();
        t.feedBits(bits);
        t.printReport(testBits);

        // 4) Örnek seed doğrulama tablosu (hocanın istediği gibi)
        // (İstersen seedleri değiştirebilirsin)
        int tabloTestBits = 100000;
        ValidationRow[] rows = new ValidationRow[] {
                validateSeed(123456789L, tabloTestBits),
                validateSeed(987654321L, tabloTestBits),
                validateSeed(20242025L,  tabloTestBits)
        };
        printValidationTable(rows, tabloTestBits);

        sc.close();
    }
}


