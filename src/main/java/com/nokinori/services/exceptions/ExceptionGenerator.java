package com.nokinori.services.exceptions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

/**
 * Class that generates exceptions.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionGenerator {

    private static final String SIM_CARD_WITH_ID = "Sim-card with id: ";
    private static final String NOT_FOUND = " not found";

    /**
     * Method throws {@link NotFoundException}.
     *
     * @param id to be in message.
     * @return supplier to be called later.
     */
    public static Supplier<NotFoundException> throwNotFoundException(Long id) {
        return () -> new NotFoundException(SIM_CARD_WITH_ID + id + NOT_FOUND);
    }

    /**
     * Method throws {@link SimCardActivationException}.
     *
     * @param id to be in message.
     */
    public static void throwSimCardActivationException(Long id) {
        throw new SimCardActivationException(SIM_CARD_WITH_ID + id + " already activated");
    }

    /**
     * Method throws {@link SimCardBlockageException}.
     *
     * @param id to be in message.
     */
    public static void throwSimCardBlockageException(Long id) {
        throw new SimCardBlockageException(SIM_CARD_WITH_ID + id + " already blocked");
    }

    /**
     * Method throws {@link SimCardBlockageException}.
     *
     * @param id to be in message.
     */
    public static void throwSimCardBlockageException(Long id, String msg) {
        throw new SimCardBlockageException(SIM_CARD_WITH_ID + id + msg);
    }

    /**
     * Method throws {@link NotFoundException}.
     *
     * @param id to be in message.
     */
    public static void throwPackNotFoundException(Long id) {
        throw new NotFoundException(SIM_CARD_WITH_ID + id + " doesn't have any packs");
    }
}

