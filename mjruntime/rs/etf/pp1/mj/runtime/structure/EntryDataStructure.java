package rs.etf.pp1.mj.runtime.structure;

import java.util.Collection;

public abstract class EntryDataStructure {

    /**
     * Dodaje novi unos u heš tabelu samo ako za dati indeks
     * već ne postoji unos.
     *
     * @param index indeks pod kojim se unosi modul
     * @param name  ime modula koje se vezuje za dati indeks
     * @return {@code true} ako je unos uspešno dodat,
     *         {@code false} ako već postoji unos sa tim indeksom
    */
    public abstract boolean addEntry(int index, String name);

    /**
     * Vraća ime modula povezano sa datim indeksom.
     *
     * @param index indeks modula koji se traži
     * @return ime modula ako indeks postoji, ili {@code null} ako ne postoji
    */
    public abstract String resolveContextIndex(int index);

    /**
     * Vraća kolekciju svih imena modula koji su uneti u strukturu.
     *
     * @return kolekcija imena modula
    */
    public abstract Collection<String> getModuleNames();
}
