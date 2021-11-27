package homework;

import actor.ActorsAwards;
import fileio.ActionInputData;

import javax.print.attribute.HashAttributeSet;
import javax.xml.crypto.Data;
import java.util.*;
import java.util.stream.Stream;

public class Queries {
    String objectType;
    Integer number;
    List<List<String>> filters;
    String sortType;
    String criteria;

    public Queries(ActionInputData action) {
        this.objectType = action.getObjectType();
        this.number = action.getNumber();
        this.filters = action.getFilters();
        this.sortType = action.getSortType();
        this.criteria = action.getCriteria();
    }

    public List<String> averageQuery(Database db) {
        List<String> actorsList = new ArrayList<>();
        Integer size = 0;
        Map<String, Double> unsorted = new HashMap<>();
        for (Map.Entry<String, Actor> entry : db.getActorMap().entrySet()) {
            unsorted.put(entry.getKey(), entry.getValue().getAverage(db.getMovieMap(), db.getSerialMap()));
        }
        Map<String, Double> sorted = sortByValue(unsorted);
        for(Map.Entry<String, Double> entry : sorted.entrySet()) {
            if (size < number) {
                actorsList.add(entry.getKey());
                size = size + 1;
            } else {
                break;
            }
        }
        return actorsList;
    }

    private Map<String, Double> sortByValue(Map<String, Double> unsorted) {
        List<Map.Entry<String, Double>> list = new LinkedList<>(unsorted.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                if (sortType.equals("asc")) {
                    if (Double.compare(o1.getValue(), o2.getValue()) == 0) {
                        return o1.getKey().compareTo(o2.getKey());
                    } else {
                       return o1.getValue().compareTo(o2.getValue());
                    }
                } else {
                    if (Double.compare(o1.getValue(), o2.getValue()) == 0) {
                        return o2.getKey().compareTo(o1.getKey());
                    } else {
                        return o2.getValue().compareTo(o1.getValue());
                    }
                }
            }
        });
        HashMap<String, Double> sorted = new LinkedHashMap<>();
        for (Map.Entry<String, Double> entry : list) {
            sorted.put(entry.getKey(), entry.getValue());
        }
        return sorted;
    }

    public List<String> awardQuery(Database db) {
        List<Actor> actorList = new ArrayList<>();
        for (Map.Entry<String, Actor> entry : db.getActorMap().entrySet()) {
            actorList.add(entry.getValue());
        }
        actorList = actorList.stream()
                    .filter(o1 -> hasAward(o1)).toList();
        return actorListToString(actorList);
    }

    private boolean hasAward(Actor actor) {
        int count = 0; // Numara cate premii din filters are actorul
        for(Map.Entry<ActorsAwards, Integer> entry : actor.getAwards().entrySet()) {
            for (String award : filters.get(3)) {
                if (entry.getKey().toString().equals(award)) {
                    count++;
                }
            }
        }
        if (count == filters.get(3).size()) {
            return true;
        }
        return false;
    }

    private List<String> actorListToString(List<Actor> actorList) {
        List<String> actorStringList = new ArrayList<>();
        for (Actor a : actorList) {
            actorStringList.add(a.getName());
        }
        return actorStringList;
    }

}
