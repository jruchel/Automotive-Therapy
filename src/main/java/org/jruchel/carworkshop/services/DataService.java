package org.jruchel.carworkshop.services;

import org.jruchel.carworkshop.models.data.Data;
import org.jruchel.carworkshop.models.data.Dataset;
import org.jruchel.carworkshop.models.data.Options;
import org.jruchel.carworkshop.models.data.Title;
import org.jruchel.carworkshop.models.entities.Order;
import org.jruchel.carworkshop.repositories.OpinionRepository;
import org.jruchel.carworkshop.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DataService {
    private final OrderRepository orderRepository;
    private final OpinionRepository opinionRepository;

    public DataService(OrderRepository orderRepository, OpinionRepository opinionRepository) {
        this.orderRepository = orderRepository;
        this.opinionRepository = opinionRepository;
    }

    public Data getOpinionsData() {
        List<Double> values = new ArrayList<>();
        for (int i = 5; i > 0; i--) {
            values.add((double) opinionRepository.getAmountOfOpinionsByRating(i));
        }

        Data data = new Data();
        data.setLabels(reverseList(Arrays.asList("1", "2", "3", "4", "5")));
        List<Dataset> datasets = new ArrayList<>();
        datasets.add(new Dataset("Opinions", reverseList(Arrays.asList("#ff1919", "#ffae19", "#ffff19", "#4ca64c", "#198c19")), values));
        data.setDatasets(datasets);
        Options options = new Options(new Title("Opinie według ocen", true), true, false);
        data.setOptions(options);
        return data;
    }

    private List<String> reverseList(List<String> list) {
        List<String> newList = new ArrayList<>();
        for (int i = list.size() - 1; i >= 0; i--) {
            newList.add(list.get(i));
        }
        return newList;
    }

    public Data getOrdersData() {
        List<Order.Status> statuses = Arrays.asList(Order.Status.values().clone());
        List<Double> values = new ArrayList<>();
        for (Order.Status status : statuses) {
            values.add((double) orderRepository.countStatus(status.toString()));
        }
        Data data = new Data();
        data.setLabels(statuses.stream().map(Enum::toString).collect(Collectors.toList()));
        List<Dataset> datasets = new ArrayList<>();
        datasets.add(new Dataset("Orders", Arrays.asList("#ffff19", "#ffae19", "#4ca64c", "#ff1919"), values));
        data.setDatasets(datasets);
        Options options = new Options(new Title("Zamówienia według stanu", true), true, false);
        data.setOptions(options);
        return data;
    }
}
