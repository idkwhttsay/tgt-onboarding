package com.daniil.tgt.controller;

import com.daniil.tgt.dto.SequenceDto;
import com.daniil.tgt.service.SequenceService;
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
    public ResponseEntity<SequenceDto> getBySeq(@PathVariable long seqNum) {
        return sequenceService.getBySeqNum(seqNum)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/next")
    public SequenceDto createNextNow() {
        return sequenceService.createNext();
    }
}
