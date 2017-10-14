import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class futuresExample {

    // helper function that sleeps.  Used to minimize code in slowReverseFuture
    // can't do Thread.sleep(t) w/o try/catch statement.
    private static void delay(long t) {
        try { Thread.sleep(t); } catch (InterruptedException e) {
            System.out.println(e);}
    }

    // simultaneously reverses str1 and str2
    // then combines the results and concatenates them
    private static String asyncSlowConcat(final String str1, final String str2) {
        CompletionStage<String> reverse1 = slowReverseFuture(str1);
        CompletionStage<String> reverse2ThenConcat = slowReverseFuture(str2)
                .thenCombineAsync(reverse1, (s2, s1) -> s1.concat(s2));

        CompletableFuture comp = reverse2ThenConcat.toCompletableFuture();
        return comp.join().toString();
    }

    // function that creates a Future out of string w/ the string reversed
    // a delay is added to make it "slow"
    private static CompletionStage<String> slowReverseFuture(String str){
        return CompletableFuture.supplyAsync(() -> {
                    delay(500);
                    return new StringBuilder(str).reverse().toString();
                }
        );
    }

    public static void main(String[] args) {
         String s = asyncSlowConcat(" olleh", "!dlrow");
         System.out.println(s);
    }
}
