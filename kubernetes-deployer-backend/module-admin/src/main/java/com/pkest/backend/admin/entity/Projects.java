package com.pkest.backend.admin.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by wuzhonggui on 2018/5/23.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
@Entity
@Table(name = "projects", schema = "deployer", catalog = "")
public class Projects {
    private int id;
    private String name;
    private String repository;
    private String hash;
    private String branch;
    private String privateKey;
    private String publicKey;
    private int buildsToKeep;
    private String url;
    private String buildUrl;
    private Timestamp lastRun;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;
    private Timestamp lastMirrored;
    private byte allowOtherBranch;
    private byte includeDev;
    private int status;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "repository", nullable = false, length = 255)
    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    @Basic
    @Column(name = "hash", nullable = false, length = 255)
    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Basic
    @Column(name = "branch", nullable = false, length = 255)
    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    @Basic
    @Column(name = "private_key", nullable = false, length = -1)
    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    @Basic
    @Column(name = "public_key", nullable = false, length = -1)
    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    @Basic
    @Column(name = "builds_to_keep", nullable = false)
    public int getBuildsToKeep() {
        return buildsToKeep;
    }

    public void setBuildsToKeep(int buildsToKeep) {
        this.buildsToKeep = buildsToKeep;
    }

    @Basic
    @Column(name = "url", nullable = true, length = 255)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Basic
    @Column(name = "build_url", nullable = true, length = 255)
    public String getBuildUrl() {
        return buildUrl;
    }

    public void setBuildUrl(String buildUrl) {
        this.buildUrl = buildUrl;
    }

    @Basic
    @Column(name = "last_run", nullable = true)
    public Timestamp getLastRun() {
        return lastRun;
    }

    public void setLastRun(Timestamp lastRun) {
        this.lastRun = lastRun;
    }

    @Basic
    @Column(name = "created_at", nullable = true)
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Basic
    @Column(name = "updated_at", nullable = true)
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Basic
    @Column(name = "deleted_at", nullable = true)
    public Timestamp getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Timestamp deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Basic
    @Column(name = "last_mirrored", nullable = true)
    public Timestamp getLastMirrored() {
        return lastMirrored;
    }

    public void setLastMirrored(Timestamp lastMirrored) {
        this.lastMirrored = lastMirrored;
    }

    @Basic
    @Column(name = "allow_other_branch", nullable = false)
    public byte getAllowOtherBranch() {
        return allowOtherBranch;
    }

    public void setAllowOtherBranch(byte allowOtherBranch) {
        this.allowOtherBranch = allowOtherBranch;
    }

    @Basic
    @Column(name = "include_dev", nullable = false)
    public byte getIncludeDev() {
        return includeDev;
    }

    public void setIncludeDev(byte includeDev) {
        this.includeDev = includeDev;
    }

    @Basic
    @Column(name = "status", nullable = false)
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Projects that = (Projects) o;

        if (id != that.id) return false;
        if (buildsToKeep != that.buildsToKeep) return false;
        if (allowOtherBranch != that.allowOtherBranch) return false;
        if (includeDev != that.includeDev) return false;
        if (status != that.status) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (repository != null ? !repository.equals(that.repository) : that.repository != null) return false;
        if (hash != null ? !hash.equals(that.hash) : that.hash != null) return false;
        if (branch != null ? !branch.equals(that.branch) : that.branch != null) return false;
        if (privateKey != null ? !privateKey.equals(that.privateKey) : that.privateKey != null) return false;
        if (publicKey != null ? !publicKey.equals(that.publicKey) : that.publicKey != null) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        if (buildUrl != null ? !buildUrl.equals(that.buildUrl) : that.buildUrl != null) return false;
        if (lastRun != null ? !lastRun.equals(that.lastRun) : that.lastRun != null) return false;
        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null) return false;
        if (updatedAt != null ? !updatedAt.equals(that.updatedAt) : that.updatedAt != null) return false;
        if (deletedAt != null ? !deletedAt.equals(that.deletedAt) : that.deletedAt != null) return false;
        if (lastMirrored != null ? !lastMirrored.equals(that.lastMirrored) : that.lastMirrored != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (repository != null ? repository.hashCode() : 0);
        result = 31 * result + (hash != null ? hash.hashCode() : 0);
        result = 31 * result + (branch != null ? branch.hashCode() : 0);
        result = 31 * result + (privateKey != null ? privateKey.hashCode() : 0);
        result = 31 * result + (publicKey != null ? publicKey.hashCode() : 0);
        result = 31 * result + buildsToKeep;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (buildUrl != null ? buildUrl.hashCode() : 0);
        result = 31 * result + (lastRun != null ? lastRun.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        result = 31 * result + (deletedAt != null ? deletedAt.hashCode() : 0);
        result = 31 * result + (lastMirrored != null ? lastMirrored.hashCode() : 0);
        result = 31 * result + (int) allowOtherBranch;
        result = 31 * result + (int) includeDev;
        result = 31 * result + status;
        return result;
    }
}
