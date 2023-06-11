package it.polito.tdp.gosales.model;

import java.time.LocalDate;
import java.util.Objects;

public class Evento implements Comparable<Evento>{

	public enum EventType{
		RIFORNIMENTO,
		VENDITA
	}
	
	private EventType type;
	private LocalDate date;
	
	public Evento(EventType type, LocalDate date) {
		super();
		this.type = type;
		this.date = date;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	@Override
	public int hashCode() {
		return Objects.hash(date, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Evento other = (Evento) obj;
		return Objects.equals(date, other.date) && type == other.type;
	}

	@Override
	public int compareTo(Evento o) {
		return this.date.compareTo(o.date);
	}
	
	
	
	
}
