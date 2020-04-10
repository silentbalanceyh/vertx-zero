package io.vertx.up.uca.job.phase;

import io.vertx.up.uca.job.plugin.JobIncome;
import io.vertx.up.uca.job.plugin.JobOutcome;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {
    ConcurrentMap<String, JobIncome> INCOMES = new ConcurrentHashMap<>();
    ConcurrentMap<String, JobOutcome> OUTCOMES = new ConcurrentHashMap<>();
    ConcurrentMap<String, Phase> PHASES = new ConcurrentHashMap<>();
}