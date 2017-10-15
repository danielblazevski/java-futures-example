import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class futuresExample {

    public class slowConcatFuture {
        // Used to minimize code later, can't do Thread.sleep(t) w/o try/catch statement.
        private void delay(long t) {
            try { Thread.sleep(t); }
            catch (InterruptedException e) {System.out.println(e);}
        }

        // simultaneously reverses two strings, combines results and concatenates them
        public String asyncSlowConcat(final String str1, final String str2) {
            CompletionStage<String> reverse1 = slowReverseFuture(str1);
            return slowReverseFuture(str2)
                    .thenCombineAsync(reverse1, (s2, s1) -> s1.concat(s2))
                    .toCompletableFuture().join();
        }

        // function that creates a Future from a string and reversing it.
        // A delay is added to make it "slow"
        private CompletionStage<String> slowReverseFuture(String str) {
            return CompletableFuture.supplyAsync(() -> {
                        delay(500);
                        return new StringBuilder(str).reverse().toString();
                    }
            );
        }
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        slowConcatFuture scf = new futuresExample().new slowConcatFuture();
        String s = scf.asyncSlowConcat(" olleh", "!dlrow");
        long endTime = System.currentTimeMillis();
        System.out.println(s);
        System.out.println(endTime - startTime);
    }
}