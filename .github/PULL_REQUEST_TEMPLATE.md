<!-- This text is NOT in the public PR template, and is removed in the cl_deploy script when doing a release. If you need to make changes to this file that aren't public, make sure you update the deploy script too. Otherwise, if you need to update the public file, make a public PR. -->
:warning: IMPORTANT :warning: READ THIS BEFORE ADDING FEATURES TO THIS SDK!

All new features must be added to the GraphQL API before adding to the server SDKs. For more information, see the [API/SDK development policy](TBD, see next section), [GraphQL API contribution guide](https://knowledge.braintree.tools/unified_api/contributing/), and stop by #service-graphql-api with any questions.

After you have merged your new features into the GraphQL API, you may PR your SDK change here and one of the approved committers will merge it upon aproval.

# Summary

# Checklist

- [ ] Added changelog entry
- [ ] Ran unit tests (`mvn verify -DskipITs`)
