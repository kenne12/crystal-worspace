import {HttpParams} from '@angular/common/http';

export interface PageableData<T> {
  content: T[];
  page: {
    size: number,
    number: number,
    totalElements: number,
    totalPages: number
  }
}

export type Pagination = {
  page: number;
  size: number;
  sort?: string[];
};

export const createPaginationOption = (req: Pagination): HttpParams => {
  let params = new HttpParams();
  params = params.append("page", req?.page || 0).append("size", req?.size || 10);

  // req?.sort.forEach(value => {
  //   params = params.set("sort", value);
  // });

  return params;
};
