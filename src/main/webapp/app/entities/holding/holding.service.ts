import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IHolding } from 'app/shared/model/holding.model';

type EntityResponseType = HttpResponse<IHolding>;
type EntityArrayResponseType = HttpResponse<IHolding[]>;

@Injectable({ providedIn: 'root' })
export class HoldingService {
    public resourceUrl = SERVER_API_URL + 'api/holdings';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/holdings';

    constructor(private http: HttpClient) {}

    create(holding: IHolding): Observable<EntityResponseType> {
        return this.http.post<IHolding>(this.resourceUrl, holding, { observe: 'response' });
    }

    update(holding: IHolding): Observable<EntityResponseType> {
        return this.http.put<IHolding>(this.resourceUrl, holding, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IHolding>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    findByCurrentUser(): Observable<EntityArrayResponseType> {
        return this.http.get<IHolding[]>(`${this.resourceUrl}/currentUser`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IHolding[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IHolding[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
