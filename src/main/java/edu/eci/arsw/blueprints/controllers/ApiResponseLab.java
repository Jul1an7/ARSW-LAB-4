package edu.eci.arsw.blueprints.controllers;

public record ApiResponseLab<T>(int code, String message, T data) {}
