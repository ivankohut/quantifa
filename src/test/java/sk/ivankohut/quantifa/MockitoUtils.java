package sk.ivankohut.quantifa;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Stubber;

import static org.mockito.Mockito.doAnswer;

public final class MockitoUtils {

    private MockitoUtils() {
        // empty
    }

    public interface VoidAnswer {

        void accept(InvocationOnMock invocation) throws Throwable;
    }

    public static Stubber doVoidAnswer(VoidAnswer voidAnswer) {
        return doAnswer(invocation -> {
            voidAnswer.accept(invocation);
            return null;
        });
    }
}
