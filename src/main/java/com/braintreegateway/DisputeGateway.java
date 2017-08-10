package com.braintreegateway;

import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;

/**
 * Provides methods to interact with {@link Dispute} objects.
 * This class does not need to be instantiated directly. Instead, use
 * {@link BraintreeGateway#dispute()} to get an instance of this class:
 *
 * <pre>
 * BraintreeGateway gateway = new BraintreeGateway(...);
 * gateway.dispute().find(...)
 * </pre>
 *
 * For more detailed information on {@link Dispute disputes}, see <a href="http://www.braintreepayments.com/guides/dispute" target="_blank">http://www.braintreepaymentsolutions.com/guides/dispute</a>
 */
public class DisputeGateway {
    private Configuration configuration;
    private Http http;

    public DisputeGateway(Http http, Configuration configuration) {
        this.configuration = configuration;
        this.http = http;
    }

    /**
     * Accept a @{link Dispute}, given a dispute ID.
     *
     * @param id the dispute id to accept.
     *
     * @return a {@link Result}.
     *
     * @throws NotFoundException if a Dispute with the given ID cannot be found.
     */
    public Result<Dispute> accept(String id) {
        try {
            if (id == null || id.trim().equals("")) {
                throw new NotFoundException();
            }

            NodeWrapper response = http.put(configuration.getMerchantPath() + "/disputes/" + id + "/accept");
            if (response.getElementName().equals("api-error-response")) {
                return new Result<Dispute>(response, Dispute.class);
            }

            return new Result<Dispute>();
        } catch (NotFoundException e) {
            throw new NotFoundException("dispute with id \"" + id + "\" not found");
        }
    }

    /**
     * Add File Evidence to a @{link Dispute}, given an ID and a @{link DocumentUpload} ID.
     *
     * @param disputeId the dispute id to add text evidence to.
     * @param documentUploadId the dispute id to accept.
     *
     * @return a {@link Result}.
     *
     * @throws NotFoundException if the Dispute ID or Document ID cannot be found.
     */
    public void addFileEvidence(String disputeId, String documentUploadId) {
        if (disputeId == null || disputeId.trim().equals("")) {
            throw new NotFoundException("dispute with id \"" + disputeId + "\" not found");
		}

        if (documentUploadId == null || documentUploadId.trim().equals("")) {
            throw new NotFoundException("document with id \"" + documentUploadId + "\" not found");
		}

    }

    /**
     * Add Text Evidence to a @{link Dispute}, given an ID and content.
     *
     * @param id the dispute id to add text evidence to.
     * @param content the text content to add to the dispute.
     *
     * @return a {@link Result}.
     *
     * @throws NotFoundException if a Dispute with the given ID cannot be found.
     * @throws IllegalArgumentException if the content is empty.
     */
    public void addTextEvidence(String id, String content) {
        if (id == null || id.trim().equals("")) {
            throw new NotFoundException("dispute with id \"" + id + "\" not found");
        }

        if (content == null || content.trim().equals("")) {
            throw new IllegalArgumentException("content cannot be empty");
        }
    }

    /**
     * Finalize a @{link Dispute}, given an ID.
     *
     * @param id the dispute id to finalize.
     *
     * @return a {@link Result}.
     *
     * @throws NotFoundException if a Dispute with the given ID cannot be found.
     */
    public Result<Dispute> finalize(String id) {
        try {
            if (id == null || id.trim().equals("")) {
                throw new NotFoundException();
            }

            NodeWrapper response = http.put(configuration.getMerchantPath() + "/disputes/" + id + "/finalize");

            if (response.getElementName().equals("api-error-response")) {
                return new Result<Dispute>(response, Dispute.class);
            }

            return new Result<Dispute>();
        } catch (NotFoundException e) {
            throw new NotFoundException("dispute with id \"" + id + "\" not found");
        }
    }

    /**
     * Returns a @{link Dispute}, given an ID.
     *
     * @param id the dispute id to find.
     *
     * @return a {@link Dispute}.
     *
     * @throws NotFoundException if a Dispute with the given ID cannot be found.
     */
    public Dispute find(String id) {
        try {
            if (id == null || id.trim().equals("")) {
                throw new NotFoundException();
            }

            return new Dispute(http.get(configuration.getMerchantPath() + "/disputes/" + id));
        } catch (NotFoundException e) {
            throw new NotFoundException("dispute with id \"" + id + "\" not found");
        }
    }

    /**
     * Remove Evidence from a @{link Dispute}, given an ID and a @{link DisputeEvidence} ID.
     *
     * @param disputeId the dispute id to add text evidence to.
     * @param evidenceId the evidence id to remove.
     *
     * @return a {@link Result}.
     *
     * @throws NotFoundException if the Dispute ID or evidence ID cannot be found.
     */
    public void removeEvidence(String disputeId, String evidenceId) {
        if (disputeId == null || disputeId.trim().equals("") || evidenceId == null || evidenceId.trim().equals("")) {
            throw new NotFoundException("evidence with id \"" + evidenceId + "\" for dispute with id \"" + disputeId + "\" not found");
		}
    }
}
