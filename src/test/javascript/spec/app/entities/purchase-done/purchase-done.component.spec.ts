/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { NewLocalTestModule } from '../../../test.module';
import { PurchaseDoneComponent } from 'app/entities/purchase-done/purchase-done.component';
import { PurchaseDoneService } from 'app/entities/purchase-done/purchase-done.service';
import { PurchaseDone } from 'app/shared/model/purchase-done.model';

describe('Component Tests', () => {
    describe('PurchaseDone Management Component', () => {
        let comp: PurchaseDoneComponent;
        let fixture: ComponentFixture<PurchaseDoneComponent>;
        let service: PurchaseDoneService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NewLocalTestModule],
                declarations: [PurchaseDoneComponent],
                providers: []
            })
                .overrideTemplate(PurchaseDoneComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(PurchaseDoneComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PurchaseDoneService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new PurchaseDone(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.purchaseDones[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
