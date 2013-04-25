package com.acme.domain.email;

public interface Queue<K> {

	public K getEmail();

	public void addEmail(K email);

	public void close();

}
