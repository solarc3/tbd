FROM node:20-slim


RUN apt-get update && \
    apt-get install -y --no-install-recommends \
      python3 \
      make \
      g++ \
      libsqlite3-dev


WORKDIR /app

COPY package*.json ./
RUN npm install \
  && npm rebuild better-sqlite3 --build-from-source

COPY . .

ENV NUXT_HOST=0.0.0.0
ENV NUXT_PORT=3000

EXPOSE 3000

CMD ["npx", "nuxi", "dev", "--host", "0.0.0.0", "--port", "3000"]
