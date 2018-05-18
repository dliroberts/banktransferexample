package asinoladro.api;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Customer {
	@NotEmpty
    private final int id;
	
	@NotEmpty
    private final String fullName;

    @JsonCreator
    public Customer(
			@JsonProperty("id") int id,
			@JsonProperty("fullName") String fullName
		) {
		this.id = id;
		this.fullName = fullName;
	}

	@JsonProperty
	public int getId() {
		return id;
	}

	@JsonProperty
	public String getFullName() {
		return fullName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", fullName=" + fullName + "]";
	}
}
