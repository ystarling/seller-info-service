Java source generates automatically and place in /target/generated-sources/com.example.generated

or manually type: mvn graphqlcodegen:generate

Flyway migration in /resources/db/migration

Access point to graphql playground:
http://localhost:8080/graphiql?path=/graphql


Graphgl template: 
```json 
{
  sellers (
    page: {page: 0, size: 20}
    filter: {	
      searchByName: "Amazon US",
      producerIds:[""],
      marketplaceIds: ["amazon.ae"]
    }
    sortBy: NAME_DESC
  ) {
    data {
      sellerName
      externalId
      marketplaceId
      producerSellerStates {
        producerId
        producerName
        sellerState
        sellerId        
      }
    }
    meta {
      page: page
    	size: size
    }
  }
}
```

psql -d sellers_db_test -f test_data.sql


