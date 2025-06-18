FROM node:22-slim

RUN apt-get update \
 && apt-get install -y --no-install-recommends \
      python3 make g++ libsqlite3-dev \
 && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY package.json package-lock.json ./

RUN npm ci

COPY . .

RUN rm -rf node_modules/better-sqlite3/build \
 && npm rebuild better-sqlite3 \
 && npm cache clean --force

ENV NUXT_HOST=0.0.0.0
ENV NUXT_PORT=3000
EXPOSE 3000

CMD ["npx", "nuxi", "dev", "--host", "0.0.0.0", "--port", "3000"]