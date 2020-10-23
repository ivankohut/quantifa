package sk.ivankohut.quantifa;

import com.ib.controller.ApiController;

import java.util.List;

@SuppressWarnings("PMD.AtLeastOneConstructor")
public class TwsConnectionHandler implements ApiController.IConnectionHandler {

    @Override
    public void connected() {
        // empty
    }

    @Override
    public void disconnected() {
        // empty
    }

    @Override
    public void accountList(List<String> list) {
        // empty
    }

    @Override
    public void error(Exception exception) {
        // empty
    }

    @Override
    public void message(int id, int errorCode, String errorMsg) {
        // empty
    }

    @Override
    public void show(String string) {
        // empty
    }
}
