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
@RequestMapping("/api/boards")
public class LongPollingController {

    public Map<Object, Consumer<Board>> listeners = new HashMap<>();

    @GetMapping(path= {"/updates"})
    public DeferredResult<ResponseEntity<?>> getUpdates() {
        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var res = new DeferredResult<ResponseEntity<?>>(1000L, noContent);

        var key = new Object();
        listeners.put(key, q -> {
            res.setResult(ResponseEntity.ok(q));
        });
        res.onCompletion(() -> listeners.remove(key));

        return res;
    }

}
