package com.daniil.tgt.store;

import com.daniil.tgt.dto.SequenceDto;
import java.util.List;
import java.util.Optional;

/* Store interface for sequence data */
public interface SequenceStore {
    void save(SequenceDto dto);
    Optional<SequenceDto> getBySeqNum(long seqNum);
    List<SequenceDto> getRangeSeq(long fromSeq, long toSeq);
    List<SequenceDto> getLatestSeq(int limit);
    boolean existsRandomString(String s); 
}