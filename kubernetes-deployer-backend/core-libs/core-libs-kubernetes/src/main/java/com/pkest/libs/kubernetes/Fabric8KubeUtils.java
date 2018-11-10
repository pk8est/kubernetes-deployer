package com.pkest.libs.kubernetes;

import com.pkest.libs.kubernetes.exception.TimeoutException;
import com.pkest.libs.kubernetes.exception.K8sDriverException;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.DaemonSet;
import io.fabric8.kubernetes.api.model.apps.DaemonSetList;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import io.fabric8.kubernetes.api.model.batch.Job;
import io.fabric8.kubernetes.api.model.batch.JobList;
import io.fabric8.kubernetes.api.model.extensions.Ingress;
import io.fabric8.kubernetes.api.model.extensions.IngressList;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.*;
import okhttp3.TlsVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * @author 360733598@qq.com
 * @date 2018/11/6 22:32
 */
public class Fabric8KubeUtils implements KubeUtils<KubernetesClient> {
    private KubernetesClient client;
    private static final int CONNECTION_TIMEOUT = 3 * 1000;
    private static final int REQUEST_TIMEOUT = 3 * 1000;

    private static Logger logger = LoggerFactory.getLogger(Fabric8KubeUtils.class);

    private Fabric8KubeUtils(KubernetesClient client) {
        this.client = client;
    }

    public static KubeUtils buildKubeUtils(Cluster cluster, String namespace, String ca, String cert, String key) throws K8sDriverException {
        if (cluster == null) {
            throw new K8sDriverException("cluster is null");
        }
        /*String key = cluster.md5Key(namespace);
        if (KUBEUTILSMAP.containsKey(key)) {
            return KUBEUTILSMAP.get(key);
        }*/
        String master = "https://47.107.13.156:6443";
        /*String master = cluster.getApi();
        master = CommonUtil.fullUrl(master);
        if (StringUtils.isBlank(master)) {
            throw new K8sDriverException("master api is null, cluster id=" + cluster.getId() + ", cluster name=" + cluster.getName());
        }*/

        Config config = null;
        if (master.toLowerCase().startsWith("https://")) {
            config = new ConfigBuilder().withMasterUrl(master)
                    .withTrustCerts(true)
                    .withNamespace(namespace)
                    .withCaCertData(ca)
                    .withClientCertData(cert)
                    .withClientKeyData(key)
                    //.withOauthToken(cluster.getOauthToken())
                    //.withUsername(cluster.getUsername())
                    //.withPassword(cluster.getPassword())
                    .removeFromTlsVersions(TlsVersion.TLS_1_0)
                    .removeFromTlsVersions(TlsVersion.TLS_1_1)
                    .removeFromTlsVersions(TlsVersion.TLS_1_2)
                    .withRequestTimeout(REQUEST_TIMEOUT)
                    .withConnectionTimeout(CONNECTION_TIMEOUT)
                    .build();
        } else {
            /*config = new ConfigBuilder().withMasterUrl(master)
                    .withNamespace(namespace)
                    .withOauthToken(cluster.getOauthToken())
                    .withUsername(cluster.getUsername())
                    .withPassword(cluster.getPassword())
                    .removeFromTlsVersions(TlsVersion.TLS_1_0)
                    .removeFromTlsVersions(TlsVersion.TLS_1_1)
                    .removeFromTlsVersions(TlsVersion.TLS_1_2)
                    .withTrustCerts(true)
                    .withRequestTimeout(REQUEST_TIMEOUT)
                    .withConnectionTimeout(CONNECTION_TIMEOUT)
                    .build();*/
        }
        KubeUtils kubeUtils = buildKubeUtils(config);
        //KUBEUTILSMAP.putIfAbsent(key, kubeUtils);
        return kubeUtils;
    }

    public static KubeUtils buildKubeUtils(Config config) throws K8sDriverException {
        KubernetesClient client;
        try {
            client = new DefaultKubernetesClient(config);
        } catch (Exception e) {
            throw new K8sDriverException("instantialize kubernetes client error");
        }
        return new Fabric8KubeUtils(client);
    }

    public KubernetesClient getClient() {
        return null;
    }

    public void setClient(KubernetesClient kubernetesClient) {

    }

    public String info() {
        return null;
    }

    public String serverAddress() {
        return null;
    }

    public String namespace() {
        return null;
    }

    public void deleteKubeUtils(Cluster cluster) throws K8sDriverException {

    }

    public NodeList listNode(Map<String, String> labelSelector) throws K8sDriverException {
        return null;
    }

    public NodeList listNode() throws K8sDriverException {
        logger.debug("list all nodes");
        try {
            return client.nodes().list();
        } catch (KubernetesClientException e) {
            throw new K8sDriverException(e.getMessage());
        }
}

    public Node nodeInfo(String nodeName) throws K8sDriverException {
        return null;
    }

    public Node labelNode(String nodeName, Map<String, String> labels) throws K8sDriverException {
        return null;
    }

    public List<Node> labelNode(List<String> nodeName, Map<String, String> labels) throws K8sDriverException {
        return null;
    }

    public Node deleteNodeLabel(String nodeName, List<String> labels) throws K8sDriverException {
        return null;
    }

    public Node annotateNode(String nodeName, Map<String, String> annotations) throws K8sDriverException {
        return null;
    }

    public Node deleteNodeAnnotation(String nodeName, List<String> annotations) throws K8sDriverException {
        return null;
    }

    public PodList listPod(Map<String, String> selectors) throws K8sDriverException {
        return null;
    }

    public PodList listPod() throws K8sDriverException {
        return null;
    }

    public PodList listAllPod(Map<String, String> selector) throws K8sDriverException {
        return null;
    }

    public PodList listAllPod() throws K8sDriverException {
        return null;
    }

    public Pod createPod(Pod pod) throws K8sDriverException {
        return null;
    }

    public Pod podInfo(String name) throws K8sDriverException {
        return null;
    }

    public Pod replacePod(String name, Pod pod) throws K8sDriverException {
        return null;
    }

    public boolean deletePod(String name) throws K8sDriverException {
        return false;
    }

    public Pod patchPod(String name, Pod pod) throws K8sDriverException {
        return null;
    }

    public ReplicationControllerList listReplicationController(Map<String, String> selector) throws K8sDriverException {
        return null;
    }

    public ReplicationControllerList listReplicationController() throws K8sDriverException {
        return null;
    }

    public ReplicationControllerList listAllReplicationController(Map<String, String> selector) throws K8sDriverException {
        return null;
    }

    public ReplicationControllerList listAllReplicationController() throws K8sDriverException {
        return null;
    }

    public ReplicationController createReplicationController(ReplicationController rc) throws K8sDriverException {
        return null;
    }

    public ReplicationController replicationControllerInfo(String name) throws K8sDriverException {
        return null;
    }

    public ReplicationController replaceReplicationController(String name, ReplicationController rc) throws K8sDriverException {
        return null;
    }

    public ReplicationController scaleReplicationController(String name, int replicas) throws K8sDriverException {
        return null;
    }

    public boolean deleteReplicationController(String rcName, boolean checkExist) throws K8sDriverException {
        return false;
    }

    public boolean deleteReplicationControllerWithoutCascading(String rcName) throws K8sDriverException {
        return false;
    }

    public ReplicationController patchReplicationController(String name, ReplicationController rc) throws K8sDriverException {
        return null;
    }

    public DeploymentList listDeployment(Map<String, String> selector) throws K8sDriverException {
        logger.debug("list deployment with selector=" + selector);
        try {
            return client.extensions().deployments().withLabels(selector).list();
        } catch (KubernetesClientException e) {
            throw new K8sDriverException(e.getMessage());
        }
    }

    public DeploymentList listDeployment() throws K8sDriverException {
        return listDeployment(new TreeMap<String, String>());
    }

    public DeploymentList listAllDeployment(Map<String, String> selector) throws K8sDriverException {
        return null;
    }

    public DeploymentList listAllDeployment() throws K8sDriverException {
        return null;
    }

    public Deployment createDeployment(Deployment deployment) throws K8sDriverException {
        if (deployment == null) {
            return null;
        }
        logger.debug("create deployment with deployment=\n" + deployment);
        try {
            return client.apps().deployments().create(deployment);
            //return client.extensions().deployments().create(deployment);
        } catch (KubernetesClientException e) {
            throw new K8sDriverException(e.getMessage());
        }
    }

    public Deployment deploymentInfo(String name) throws K8sDriverException {
        return null;
    }

    public Deployment replaceDeployment(String name, Deployment deployment) throws K8sDriverException {
        return null;
    }

    public boolean deleteDeployment(String deploymentName, boolean checkExist) throws K8sDriverException {
        return false;
    }

    public Deployment patchDeployment(String deploymentName, Deployment deployment) throws K8sDriverException {
        return null;
    }

    public Deployment scaleDeployment(String name, int replicas) throws K8sDriverException {
        return null;
    }

    public void pauseDeployment(String name) throws K8sDriverException {

    }

    public void resumeDeployment(String name) throws K8sDriverException {

    }

    public DaemonSetList listDaemonSet(Map<String, String> selector) throws K8sDriverException {
        return null;
    }

    public DaemonSetList listDaemonSet() throws K8sDriverException {
        return null;
    }

    public DaemonSetList listAllDaemonSet(Map<String, String> selector) throws K8sDriverException {
        return null;
    }

    public DaemonSetList listAllDaemonSet() throws K8sDriverException {
        return null;
    }

    public DaemonSet createDaemonSet(DaemonSet daemonSet) throws K8sDriverException {
        return null;
    }

    public DaemonSet daemonSetInfo(String name) throws K8sDriverException {
        return null;
    }

    public DaemonSet replaceDaemonSet(String name, DaemonSet daemonSet) throws K8sDriverException {
        return null;
    }

    public boolean deleteDaemonSet(String daemonSetName, boolean checkExist) throws K8sDriverException {
        return false;
    }

    public DaemonSet patchDaemonSet(String daemonSetName, DaemonSet daemonSet) throws K8sDriverException {
        return null;
    }

    public PersistentVolumeList listAllPersistentVolume(Map<String, String> selector) throws K8sDriverException {
        return null;
    }

    public PersistentVolumeList listAllPersistentVolume() throws K8sDriverException {
        return null;
    }

    public PersistentVolume createPersistentVolume(PersistentVolume persistentVolume) throws K8sDriverException {
        return null;
    }

    public PersistentVolume persistentVolumeInfo(String name) throws K8sDriverException {
        return null;
    }

    public PersistentVolume replacePersistentVolume(String name, PersistentVolume persistentVolume) throws K8sDriverException {
        return null;
    }

    public boolean deletePersistentVolume(String persistentVolume) throws K8sDriverException {
        return false;
    }

    public PersistentVolume patchPersistentVolume(String persistentVolumeName, PersistentVolume persistentVolume) throws K8sDriverException {
        return null;
    }

    public JobList listAllJob(Map<String, String> selector) throws K8sDriverException {
        return null;
    }

    public JobList listAllJob() throws K8sDriverException {
        return null;
    }

    public JobList listJob(Map<String, String> selector) throws K8sDriverException {
        return null;
    }

    public JobList listJob() throws K8sDriverException {
        return null;
    }

    public Job createJob(Job job) throws K8sDriverException {
        return null;
    }

    public Job getJob(String jobName) throws K8sDriverException {
        return null;
    }

    public Job jobInfo(String jobName) throws K8sDriverException {
        return null;
    }

    public Job replaceJob(String jobName, Job job) throws K8sDriverException {
        return null;
    }

    public boolean deleteJob(String jobName) throws K8sDriverException {
        return false;
    }

    public boolean deleteJob(Map<String, String> selector) throws K8sDriverException {
        return false;
    }

    public Job patchJob(String jobName, Job job) throws K8sDriverException {
        return null;
    }

    public Closeable tailfLog(String podName, String containerName, int tailingLines) throws K8sDriverException {
        return null;
    }

    public NamespaceList listAllNamespace(Map<String, String> selector) throws K8sDriverException {
        return null;
    }

    public NamespaceList listAllNamespace() throws K8sDriverException {
        return null;
    }

    public Namespace createNamespace(Namespace namespace) throws K8sDriverException {
        logger.debug("create namespace=" + namespace);
        if (namespace == null) {
            return null;
        }
        try {
            return client.namespaces().create(namespace);
        } catch (KubernetesClientException e) {
            throw new K8sDriverException(e.getMessage());
        }
    }

    public boolean deleteNamespace(String namespaceName) throws K8sDriverException {
        return false;
    }

    public ServiceList listService(Map<String, String> selector) throws K8sDriverException {
        return null;
    }

    public ServiceList listService() throws K8sDriverException {
        return null;
    }

    public Service createService(Service service) throws K8sDriverException {
        return null;
    }

    public Service serviceInfo(String serviceName) throws K8sDriverException {
        return null;
    }

    public Service replaceService(String serviceName, Service service) throws K8sDriverException {
        return null;
    }

    public boolean deleteService(String serviceName) throws K8sDriverException {
        return false;
    }

    public Service patchService(String serviceName, Service service) throws K8sDriverException {
        return null;
    }

    public ServiceList listAllService(Map<String, String> selector) throws K8sDriverException {
        return null;
    }

    public ServiceList listAllService() throws K8sDriverException {
        return null;
    }

    public EventList listAllEvent(Map<String, String> selector) throws K8sDriverException {
        return null;
    }

    public EventList listAllEvent() throws K8sDriverException {
        return null;
    }

    public EventList listEvent(Map<String, String> selector) throws K8sDriverException {
        return null;
    }

    public EventList listEvent() throws K8sDriverException {
        return null;
    }

    public EndpointsList listAllEndpoints(Map<String, String> selector) throws K8sDriverException {
        return null;
    }

    public EndpointsList listAllEndpoints() throws K8sDriverException {
        return null;
    }

    public EndpointsList listEndpoints(Map<String, String> selector) throws K8sDriverException {
        return null;
    }

    public EndpointsList listEndpoints() throws K8sDriverException {
        return null;
    }

    public Endpoints endpointsInfo(String endpointName) throws K8sDriverException {
        return null;
    }

    public Secret createSecret(Secret secret) throws K8sDriverException {
        return null;
    }

    public Secret secretInfo(String name) throws K8sDriverException {
        return null;
    }

    public boolean deleteSecret(String name) throws K8sDriverException {
        return false;
    }

    public void clearNotRunningPod(PodList podList) throws K8sDriverException {

    }

    public void clearNotRunningPodAndWait(Map<String, String> selector, long interBreak, long timeout) throws TimeoutException, K8sDriverException {

    }

    public void deleteService(Map<String, String> selector) throws TimeoutException, K8sDriverException {

    }

    public void createEndpoints(String name, List<String> addresses) throws K8sDriverException {

    }

    public void createSecret(String name, String type, Map<String, String> secretData) throws K8sDriverException {

    }

    public PersistentVolumeClaim createPersistentVolumeClaim(PersistentVolumeClaim persistentVolumeClaim) throws K8sDriverException {
        return null;
    }

    public PersistentVolumeClaim persistentVolumeClaimInfo(String name) throws K8sDriverException {
        return null;
    }

    public void deletePersistentVolumeClaim(String name) throws K8sDriverException {

    }

    public void deleteEndpoints(String name) throws K8sDriverException {

    }

    public ConfigMap createConfigmap(ConfigMap configMap) throws K8sDriverException {
        return null;
    }

    public void deleteConfigmap(Map<String, String> stringStringMap) throws K8sDriverException {

    }

    public void patchConfigmap(ConfigMap configMap) throws K8sDriverException {

    }

    public IngressList listAllIngress(Map<String, String> selector) throws K8sDriverException {
        return null;
    }

    public IngressList listAllIngress() throws K8sDriverException {
        return null;
    }

    public IngressList listIngress(Map<String, String> selector) throws K8sDriverException {
        return null;
    }

    public IngressList listIngress() throws K8sDriverException {
        return null;
    }

    public Ingress createIngress(Ingress ingress) throws K8sDriverException {
        return null;
    }

    public Ingress ingressInfo(String ingressName) throws K8sDriverException {
        return null;
    }

    public Ingress replaceIngress(String ingressName, Ingress ingress) throws K8sDriverException {
        return null;
    }

    public boolean deleteIngress(String ingressName) throws K8sDriverException {
        return false;
    }

    public boolean deleteIngress(Map<String, String> selector) throws K8sDriverException {
        return false;
    }

    public Ingress patchIngress(String ingressName, Ingress ingress) throws K8sDriverException {
        return null;
    }
}

