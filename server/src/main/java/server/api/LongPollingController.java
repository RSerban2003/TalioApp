package server.api;

import commons.Board;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@RestController
@RequestMapping("/api/boards/polling")
public class LongPollingController {

    public Map<Object, Consumer<Board>> listeners = new HashMap<>();

    @GetMapping(path= {"/updates"})
    public DeferredResult<ResponseEntity<Board>> getUpdates() {
        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var res = new DeferredResult<ResponseEntity<Board>>(1000L, noContent);

        var key = new Object();
        listeners.put(key, b -> {
            res.setResult(ResponseEntity.ok(b));
        });
        res.onCompletion(() -> listeners.remove(key));

        return res;
    }

}

