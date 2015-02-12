package com.buttercoin.api;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableSet;

import java.util.Iterator;
import java.util.List;

public class Permissions implements Iterable<String> {
    private List<String> permissions;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Permissions strings = (Permissions) o;

        return !(permissions != null ? !permissions.equals(strings.permissions) : strings.permissions != null);
    }

    @Override
    public int hashCode() {
        return permissions != null ? permissions.hashCode() : 0;
    }

    @Override
    public Iterator<String> iterator() {
        return permissions != null ? permissions.iterator() : ImmutableSet.<String>of().iterator();
    }

    public List<String> getPermissions() {
        return permissions;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("permissions", permissions).toString();
    }
}
