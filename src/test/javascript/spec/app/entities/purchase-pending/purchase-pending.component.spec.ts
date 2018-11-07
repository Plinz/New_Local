/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { NewLocalTestModule } from '../../../test.module';
import { PurchasePendingComponent } from 'app/entities/purchase-pending/purchase-pending.component';
import { PurchasePendingService } from 'app/entities/purchase-pending/purchase-pending.service';
import { PurchasePending } from 'app/shared/model/purchase-pending.model';

describe('Component Tests', () => {
    describe('PurchasePending Management Component', () => {
        let comp: PurchasePendingComponent;
        let fixture: ComponentFixture<PurchasePendingComponent>;
        let service: PurchasePendingService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NewLocalTestModule],
                declarations: [PurchasePendingComponent],
                providers: []
            })
                .overrideTemplate(PurchasePendingComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(PurchasePendingComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PurchasePendingService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new PurchasePending(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.purchasePendings[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
