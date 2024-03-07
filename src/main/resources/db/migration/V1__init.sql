CREATE TABLE public.producers(
  id UUID NOT NULL CONSTRAINT "producersPK" PRIMARY KEY,
  name VARCHAR(255),
  created_at TIMESTAMP NOT NULL
);

CREATE TABLE public.marketplaces(
  id          VARCHAR(255) NOT NULL CONSTRAINT "marketplacesPK" PRIMARY KEY,
  description VARCHAR(255)
);

CREATE TABLE public.seller_infos(
  id UUID NOT NULL CONSTRAINT "seller_infosPK" PRIMARY KEY,
  marketplace_id VARCHAR(255) CONSTRAINT "FKr8ekbqgwa3g0uhgbaa1tchf09" REFERENCES public.marketplaces,
  name           VARCHAR(2048) NOT NULL,
  url            VARCHAR(2048),
  country        VARCHAR(255),
  external_id    VARCHAR(255),
  CONSTRAINT "UK12xaxk0c1mwxr3ovycs1qxtbk" UNIQUE (marketplace_id, external_id)
);

CREATE TABLE public.sellers(
  id UUID NOT NULL CONSTRAINT "marketplace_sellersPK" PRIMARY KEY,
  producer_id UUID NOT NULL CONSTRAINT "FK6y70nxr3lhubusfq6ub427ien" REFERENCES public.producers,
  seller_info_id UUID CONSTRAINT "FKp2fkfcqcndx9x9xkhk5va3cq4" REFERENCES public.seller_infos,
  state VARCHAR(255) DEFAULT 'REGULAR'::CHARACTER varying NOT NULL
);