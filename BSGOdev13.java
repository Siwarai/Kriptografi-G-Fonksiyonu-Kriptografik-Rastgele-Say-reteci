import java.util.*;

public class BSGOdev13 {
    // ---------------- G(seed) ----------------
    static class G {
        private long x;

        // Bernoulli / Riccati parametreleri (sabitler)
        // İstersen bunları değiştirerek farklı generator varyantları üretebilirsin.
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

            // Parçalı dönüşüm fikri (Collatz'taki "çift/tek" ayrımı korunuyor)
            if ((x & 1L) == 0L) {
                // Bernoulli tarzı ayrık dönüşüm: x_{n+1} = a*x_n + b
                // (long taşması doğal olarak mod 2^64 gibi davranır)
                x = a * x + b;
            } else {
                // Riccati tarzı ayrık dönüşüm: x_{n+1} = x_n^2 + c*x_n + d
                x = x * x + c * x + d;
            }

            // Kısa karıştırma (bit yayılımı / patern azaltma)
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

    // ---------------- Basit Testler ----------------
    static class Tests {
        long zeros = 0, ones = 0;
        long runs = 0;
        long[] pairs = new long[4]; // 00,01,10,11

        void feedBits(int[] bits) {
            if (bits.length == 0) return;

            int prev = bits[0];
            if (prev == 0) zeros++;
            else ones++;
            runs = 1; // ilk run

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

        void printReport(int n) {
            System.out.println("\n--- ISTATISTIK RAPORU ---");
            System.out.println("Toplam bit : " + n);
            System.out.println("0 sayisi   : " + zeros);
            System.out.println("1 sayisi   : " + ones);
            System.out.printf("1 orani    : %.6f%n", (ones / (double) n));
            System.out.println("Runs       : " + runs + " (ne kadar 'degisim' var gosterir)");
            System.out.println("2-bit pairs (00,01,10,11): "
                    + pairs[0] + ", " + pairs[1] + ", " + pairs[2] + ", " + pairs[3]);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Seed gir: ");
        long seed = sc.nextLong();

        System.out.print("Anahtar uzunlugu (bit) (ornegin 128/256): ");
        int keyLen = sc.nextInt();

        System.out.print("Test icin kac bit uretelim? (ornegin 100000): ");
        int testBits = sc.nextInt();

        G g = new G(seed);

        // 1) Anahtar üret
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

        sc.close();
    }
}


