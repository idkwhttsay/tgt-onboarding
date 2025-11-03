package com.daniil.tgt.controller;

import com.daniil.tgt.dto.SequenceDto;
import com.daniil.tgt.service.SequenceService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seq")
public class SequenceController {
    private final SequenceService sequenceService;

    public SequenceController(SequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }

    @GetMapping("/{seqNum}")
    public ResponseEntity<SequenceDto> getBySeq(@PathVariable Long seqNum) {
        if(seqNum == null || seqNum < 0) {
            return ResponseEntity.badRequest().build();
        }

        return sequenceService.getBySeqNum(seqNum)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/range")
    public ResponseEntity<List<SequenceDto>> getRangeSeq(
        @RequestParam(name="from", required = false) Long from,
        @RequestParam(name="to", required = false) Long to
    ) {
        if(from == null && to == null) {
            return ResponseEntity.badRequest().build();
        }
        
        if(from == null) from = 0L;
        if(to == null) to = Long.MAX_VALUE;

        if(from < 0 || to < 0 || from > to) {
            return ResponseEntity.badRequest().build();
        }

        return sequenceService.getRangeSeq(from, to)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/latest")
    public ResponseEntity<List<SequenceDto>> getLatest(
        @RequestParam(name="limit", required = true) Integer limit
    ) {
        if(limit == null || limit < 0) {
            return ResponseEntity.badRequest().build();
        }

        return sequenceService.getLatestSeq(limit)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/next")
    public ResponseEntity<SequenceDto> createNextNow() {
        return ResponseEntity.ok(sequenceService.createNext());
    }
}
