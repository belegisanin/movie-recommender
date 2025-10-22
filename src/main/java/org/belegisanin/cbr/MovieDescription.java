package org.belegisanin.cbr;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MovieDescription implements CaseComponent {

    private String title;
    private String director;
    private int year;

    private List<String> genres = new ArrayList<>();
    private List<String> actors = new ArrayList<>();

    public MovieDescription(String title) {
        this.title = title;
    }

    @Override
    public Attribute getIdAttribute() {
        return new Attribute("title", this.getClass());
    }

    public void addGenre(String genre) {
        if (!genres.contains(genre))
            genres.add(genre);
    }

    public void addActor(String actor) {
        if (!actors.contains(actor))
            actors.add(actor);
    }
}
