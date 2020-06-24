package life.qbic.metastats.request

import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi
import ch.ethz.sis.openbis.generic.dssapi.v3.IDataStoreServerApi
import ch.systemsx.cisd.common.spring.HttpInvokerUtils

class OpenBisSession {

    String sessionToken
    IApplicationServerApi v3
    IDataStoreServerApi dss

    /**
     * Creates an openbis session for a given user for an openbis instance
     * @param user defined by its username
     * @param password for the given user
     * @param baseURL of the openbis instance
     */
    OpenBisSession(String user, String password, String baseURL) {
        String as_url = baseURL + "/openbis/openbis" + IApplicationServerApi.SERVICE_URL
        String ds_url = baseURL + ":444" + "/datastore_server" + IDataStoreServerApi.SERVICE_URL

        v3 = HttpInvokerUtils.createServiceStub(IApplicationServerApi.class, as_url, 10000)
        dss = HttpInvokerUtils.createStreamSupportingServiceStub(IDataStoreServerApi.class,
                ds_url, 10000)

        sessionToken = v3.login(user, password)
    }

    /**
     * Closes the session
     * @return
     */
    void logout() {
        v3.logout(sessionToken)
    }
}
