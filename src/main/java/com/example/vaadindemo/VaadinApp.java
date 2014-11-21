package com.example.vaadindemo;

import com.example.vaadindemo.domain.Movie;
import com.example.vaadindemo.service.MovieManager;
import com.vaadin.annotations.Title;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

@Title("Filmy")
public class VaadinApp extends UI {

    private static final long serialVersionUID = 1L;

    private final MovieManager movieManager = new MovieManager();

    private final Movie movie = new Movie("Pulp Fiction", "komedia", "1980");
    private final BeanItem<Movie> movieItem = new BeanItem<Movie>(movie);

    private final BeanItemContainer<Movie> movies = 
            new BeanItemContainer<Movie>(Movie.class);

    enum Action {
        EDIT, ADD;
    }

    private class MyFormWindow extends Window {
        private static final long serialVersionUID = 1L;

        private Action action;

        public MyFormWindow(Action act) {
            this.action = act;

            setModal(true);
            center();

            switch (action) {
            case ADD:
                setCaption("Dodaj nowy film");
                break;
            case EDIT:
                setCaption("Edytuj film");
                break;
            default:
                break;
            }


            final FormLayout form = new FormLayout();
            final FieldGroup binder = new FieldGroup(movieItem);
            Button saveBtn = null;
            
            switch(action)
            {
                case ADD:
                    saveBtn = new Button(" Dodaj film ");
                    break;
                    
                case EDIT:
                    saveBtn = new Button(" Edytuj film ");
                    break;
            }

            final Button cancelBtn = new Button(" Anuluj ");

            // do opcji 2
            ComboBox combobox = new ComboBox("Gatunek");
            combobox.setInvalidAllowed(false);
            combobox.setNullSelectionAllowed(false);
            combobox.addItem("dramat");
            combobox.addItem("komedia");
            combobox.addItem("horror");
            combobox.addItem("obyczajowy");
            binder.bind(combobox, "genre");
            
            form.addComponent(binder.buildAndBind("Nazwa", "name"));
            //form.addComponent(binder.buildAndBind("Gatunek", "genre")); // opcja 1
            form.addComponent(combobox); // opcja 2
            form.addComponent(binder.buildAndBind("Produkcja", "production"));
            binder.setBuffered(true);

            binder.getField("name").setRequired(true);
            binder.getField("genre").setRequired(true);
            binder.getField("production").setRequired(true);

            VerticalLayout fvl = new VerticalLayout();
            fvl.setMargin(true);
            fvl.addComponent(form);

            HorizontalLayout hl = new HorizontalLayout();
            hl.addComponent(saveBtn);
            hl.addComponent(cancelBtn);
            fvl.addComponent(hl);

            setContent(fvl);

            saveBtn.addClickListener(new ClickListener() {

                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(ClickEvent event) {
                    try {
                        binder.commit();

                        if (action == Action.ADD) {
                            movieManager.addMovie(movie);
                        } else if (action == Action.EDIT) {
                            movieManager.updateMovie(movie);
                        }

                        movies.removeAllItems();
                        movies.addAll(movieManager.findAll());
                        close();
                    } catch (CommitException e) {
                        e.printStackTrace();
                    }
                }
            });

            cancelBtn.addClickListener(new ClickListener() {

                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(ClickEvent event) {
                    binder.discard();
                    close();
                }
            });
        }
    }

    @Override
    protected void init(VaadinRequest request) {

        Button addMovieFormBtn = new Button("Add");
        Button deleteMovieFormBtn = new Button("Delete");
        Button editMovieFormBtn = new Button("Edit");

        VerticalLayout vl = new VerticalLayout();
        setContent(vl);

        addMovieFormBtn.addClickListener(new ClickListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                addWindow(new MyFormWindow(Action.ADD));
            }
        });

        editMovieFormBtn.addClickListener(new ClickListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                addWindow(new MyFormWindow(Action.EDIT));
            }
        });

        deleteMovieFormBtn.addClickListener(new ClickListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                if (!movie.getName().isEmpty()) {
                    movieManager.delete(movie);
                    movies.removeAllItems();
                    movies.addAll(movieManager.findAll());
                }
            }
        });

        HorizontalLayout hl = new HorizontalLayout();
        hl.addComponent(addMovieFormBtn);
        hl.addComponent(editMovieFormBtn);
        hl.addComponent(deleteMovieFormBtn);

        final Table moviesTable = new Table("Movies", movies);
        moviesTable.setColumnHeader("name", "Nazwa filmu");
        moviesTable.setColumnHeader("genre", "Gatunek");
        moviesTable.setColumnHeader("production", "Produkcja");
        moviesTable.setSelectable(true);
        moviesTable.setImmediate(true);

        moviesTable.addValueChangeListener(new Property.ValueChangeListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(ValueChangeEvent event) {

                Movie selectedMovie = (Movie) moviesTable.getValue();
                if (selectedMovie == null) {
                    movie.setName("");
                    movie.setGenre("");
                    movie.setProduction("");
                    movie.setId(null);
                } else {
                    movie.setName(selectedMovie.getName());
                    movie.setGenre(selectedMovie.getGenre());
                    movie.setProduction(selectedMovie.getProduction());
                    movie.setId(selectedMovie.getId());
                }
            }
        });

        vl.addComponent(hl);
        vl.addComponent(moviesTable);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label();
        horizontalLayout.addComponent(label);
        label.setValue(UI.getCurrent().toString());

        vl.addComponent(horizontalLayout);
    }
}
