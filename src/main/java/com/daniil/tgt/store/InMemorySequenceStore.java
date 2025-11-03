package com.daniil.tgt.store;

import com.daniil.tgt.dto.SequenceDto;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Repository
public class InMemorySequenceStore {
    // thread-safe in-memory storage
    private final ConcurrentNavigableMap<Long, SequenceDto> map = new ConcurrentSkipListMap<>();
    private final ConcurrentSkipListSet<String> randomStrings = new ConcurrentSkipListSet<>();

    public void save(SequenceDto dto) {
        map.put(dto.getSeqNum(), dto);
        randomStrings.add(dto.getRandomStr());
    }

    public Optional<SequenceDto> getBySeqNum(long seqNum) {
        return Optional.ofNullable(map.get(seqNum));
    }

    public List<SequenceDto> getRangeSeq(long fromSeq, long toSeq) {
        return new ArrayList<>(map.subMap(fromSeq, toSeq + 1).values());
    }

    public List<SequenceDto> getLatestSeq(int limit) {
        List<SequenceDto> ret = new ArrayList<>();
        Iterator<SequenceDto> it = map.descendingMap().values().iterator();
        while(it.hasNext() && ret.size() < limit) {
            ret.add(it.next());
        }

        Collections.reverse(ret);
        return ret;
    }

    public boolean existsRandomString(String s) {
        return randomStrings.contains(s);
    }
}
